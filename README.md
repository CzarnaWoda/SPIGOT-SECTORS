# ⚔️ SPIGOT-SECTORS

> System wielosektorowy dla Minecraft 1.8.8 z **Redis Pub/Sub** - obsługa 2000+ graczy jednocześnie

[![Minecraft](https://img.shields.io/badge/Minecraft-1.8.8-brightgreen.svg)](https://www.spigotmc.org/)
[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Redis](https://img.shields.io/badge/Redis-Powered-red.svg)](https://redis.io/)
[![Redisson](https://img.shields.io/badge/Redisson-3.x-blue.svg)](https://redisson.org/)

## 🎯 O Projekcie

Produkcyjny system sektorów dla serwera minecraft, który obsługiwał **2000+ concurrent players** poprzez Redis-based distributed architecture.

## 🏗️ Architektura

```
                 Redis Cluster
                 ┌──────────────┐
                 │ RTopic (Pub/Sub)
                 │ RMap (Storage)
                 │ RSet (Players)
                 └───────┬────────┘
                         │
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐
    │ Sector  │    │ Sector  │    │ Sector  │
    │  PVP-1  │    │  PVP-2  │    │  SPAWN  │
    └─────────┘    └─────────┘    └─────────┘
    500 players    500 players    500 players
```

**BungeeCord** - Load balancing & player routing  
**Redis** - Message broker & data persistence  
**Redisson** - Java client z distributed structures

## 🛠️ Tech Stack

- **Spigot 1.8.8** - Server core
- **Redis 5.0+** - Central hub
- **Redisson 3.x** - Redis client
- **BungeeCord** - Proxy layer
- **Gson** - JSON serialization
- **Maven** - Build tool

## 📦 Kluczowa Struktura

```
pl.supereasy.sectors/
│
├── SectorPlugin.java              # Main class
│
├── api/
│   ├── packets/
│   │   ├── api/
│   │   │   ├── Packet.java        # Base packet (DTO)
│   │   │   └── PacketHandler.java # Handler interface
│   │   └── impl/
│   │       ├── PacketHandlerImpl.java  # ⭐ Cała logika tutaj
│   │       ├── PacketManager.java      # Packet ID registry
│   │       ├── synchro/           # Player sync packets
│   │       ├── guild/             # Guild packets
│   │       └── global/            # Global packets
│   │
│   ├── redis/
│   │   ├── channels/
│   │   │   └── RedisChannel.java  # ⭐ Redis structures
│   │   ├── listeners/
│   │   │   ├── PacketListener.java      # Global
│   │   │   ├── SectorPacketListener.java # Per-sector
│   │   │   └── UserListener.java        # Player transfers
│   │   └── impl/
│   │       └── RedisManagerImpl.java
│   │
│   ├── netty/
│   │   └── SectorClientImpl.java  # ⭐ Wysyłka packets
│   │
│   └── sectors/
│       └── managers/SectorManager.java
│
├── core/                           # Game features
│   ├── user/                       # Player management
│   ├── combat/                     # Combat system
│   ├── commands/                   # Commands
│   └── ...
│
├── guilds/                         # Guild system
│   ├── Guild.java                  # Guild class
│   ├── impl/GuildManagerImpl.java
│   └── ...
│
├── proxies/                        # Proxy tracking
│   ├── Proxy.java
│   └── ProxyManager.java
│
└── config/                         # Configuration
    └── api/Configurable.java
```

## ⚙️ Jak Działa Komunikacja

### 1. Packet Structure (DTO only)

```java
// Packet = prosty DTO, bez logiki
public class GuildMessagePacket extends Packet {
    private final String guildTag;
    private final String message;
    
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);  // tylko deleguje
    }
}
```

### 2. Handler (cała logika)

```java
// PacketHandlerImpl.java - TUTAJ jest logika
@Override
public void handle(GuildMessagePacket guildMessagePacket) {
    final Guild guild = plugin.getGuildManager()
                              .getGuild(guildMessagePacket.getGuildTag());
    if (guild != null) {
        guild.sendGuildMessage(guildMessagePacket.getMessage());
    }
}
```

### 3. Wysyłka (Redis Pub/Sub)

```java
// SectorClientImpl.java
@Override
public void sendGlobalPacket(Packet packet) {
    final int packetID = plugin.getPacketManager()
                               .getPacketID(packet.getClass());
    plugin.getRedisManager()
          .getPubSub(PubSubType.PACKETS)
          .sendMessage(packetID + "@" + gson.toJson(packet));
}
```

### 4. Odbiór (Redis Listener)

```java
// PacketListener.java
this.redisTopic.addListener(String.class, (channel, message) -> {
    // message format: "123@{json}"
    String[] parts = message.split("@", 2);
    int packetID = Integer.parseInt(parts[0]);
    String json = parts[1];
    
    Class<? extends Packet> clazz = packetManager.getPacket(packetID);
    Packet packet = gson.fromJson(json, clazz);
    
    packet.handlePacket(packetHandler);  // wywołuje handler
});
```

## 🔄 Flow Przykładów

### Guild Chat Cross-Sector

```
Player na PVP-1: /g chat Hello!
    ↓
Command tworzy: GuildMessagePacket("ABC", "Hello!")
    ↓
sectorClient.sendGlobalPacket(packet)
    ↓
Redis globalPacketTopic.publish("123@{guildTag:ABC,message:Hello}")
    ↓
PacketListener na WSZYSTKICH sektorach otrzymuje
    ↓
Deserializuje → GuildMessagePacket
    ↓
packet.handlePacket(handler) → handler.handle(packet)
    ↓
PacketHandlerImpl:
  Guild guild = guildManager.getGuild("ABC")
  guild.sendGuildMessage("Hello!")
    ↓
Wysyła do lokalnych członków gildii ABC
```

### Player Transfer

```
Player crosses border (X: 995 → 1005)
    ↓
SectorBorderListener wykrywa
    ↓
Serialize: inventory, location, HP, effects, guild
    ↓
UserChangeSectorPacket(serializedUser, "ABC")
    ↓
Redis publish do "PVP-2-user" topic
    ↓
BungeeCord: player.connect("PVP-2-server")
    ↓
UserListener na PVP-2:
  User u = packet.getUser()
  userManager.registerUser(u)
  Guild g = guildManager.getGuild("ABC")
  u.setGuild(g)
  u.applyValuesToPlayer()  // restore inv, HP, effects
    ↓
Player fully loaded (~200ms)
```

### Guild Destroy

```java
// Handler example z Twojego kodu:
@Override
public void handle(GuildDestroyPacket guildDestroyPacket) {
    final Guild targetGuild = plugin.getGuildManager()
                                    .getGuild(guildDestroyPacket.getTargetGuild());
    if (targetGuild != null) {
        // Remove guild from all members
        for (UUID memberUUID : targetGuild.getMembers().keySet()) {
            final User user = plugin.getUserManager().getUser(memberUUID);
            user.setGuild(null);
            
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                plugin.getTagManager().updateBoard(p);
            }
        }
        
        // Delete guild
        plugin.getGuildManager().deleteGuild(targetGuild);
        
        // Broadcast
        String msg = GuildConfig.INSTANCE.MESSAGES_GUILDDESTROY
            .replace("{GUILD}", guildDestroyPacket.getAttackerGuild())
            .replace("{GUILDVICTIM}", targetGuild.getTag());
        Bukkit.broadcastMessage(ChatUtil.fixColor(msg));
    }
}
```

## 💾 Redis Structures

```java
// RedisChannel.java - definicje struktur
public class RedisChannel {
    // Pub/Sub Topics
    public RTopic globalPacketTopic;      // Broadcast
    public RTopic currentSectorTopic;     // Per-sector
    public RTopic currentSectorUser;      // Player transfers
    
    // Persistent Maps
    public RMap<String, String> GUILDS;   // Guild data
    public RMap<UUID, String> USERS;      // User data
    public RMap<String, String> WARPS;    // Warps
    public RMap<UUID, String> ALLIANCES;  // Alliances
    
    // Online tracking
    public RSet<String> onlineGlobalPlayers;
}
```

**Usage:**
```java
// Save guild
guild.insert(true);
// → RedisChannel.INSTANCE.GUILDS.putAsync(tag, gsonJson)

// Load guild
String json = RedisChannel.INSTANCE.GUILDS.get("ABC");
Guild guild = GsonUtil.fromJson(json, Guild.class);
```

## 🚀 Key Features

**Communication:**
- ✅ Redis Pub/Sub messaging
- ✅ Global & sector-specific packets
- ✅ Player transfer sync
- ✅ Persistent data (RMap)
- ✅ BungeeCord integration

**Game Systems:**
- ✅ Guild system (territories, treasury, wars)
- ✅ Alliance system
- ✅ Combat tag / anti-logout
- ✅ Achievement system
- ✅ Boss system
- ✅ Shop & Kit system
- ✅ Leaderboards (Redis sync)
- ✅ Custom crafting & drop
- ✅ TNT management
- ✅ Auto farmer
- ✅ Stone generators

## 📊 Performance

| Metric | Value |
|--------|-------|
| Max per sector | 500 players |
| Total capacity | 2000+ concurrent |
| Redis latency | <5ms (LAN) |
| Transfer time | <200ms avg |
| TPS stability | 19.8-20.0 |

## 🔧 Custom Packet Example

```java
// 1. Create DTO
public class MyCustomPacket extends Packet {
    private final UUID targetPlayer;
    private final String data;
    
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}

// 2. Add handler method
@Override
public void handle(MyCustomPacket packet) {
    Player p = Bukkit.getPlayer(packet.getTargetPlayer());
    if (p != null && p.isOnline()) {
        p.sendMessage("Data: " + packet.getData());
    }
}

// 3. Register in PacketManager
packetManager.registerPacket(123, MyCustomPacket.class);

// 4. Send
MyCustomPacket packet = new MyCustomPacket(uuid, "Hello");
sectorClient.sendGlobalPacket(packet);  // all sectors
// lub
sectorClient.sendPacket(packet, "PVP-2");  // specific sector
```

## 📝 Instalacja

**Wymagania:**
- Java 8
- Redis 5.0+
- BungeeCord
- Spigot 1.8.8

**Setup:**
```bash
# Redis
sudo apt-get install redis-server
sudo systemctl start redis

# Build
git clone https://github.com/CzarnaWoda/SPIGOT-SECTORS.git
cd SPIGOT-SECTORS
mvn clean package
```

**Config (sector.yml):**
```yaml
sector:
  name: "PVP-1"
  type: PVP

redis:
  server: "localhost"
  password: ""

borders:
  min-x: -1000
  max-x: 1000
  min-z: -1000
  max-z: 1000
```

## 🐛 Known Issues

- ⚠️ Dependency cleaned z POM (proprietary libs)
- ⚠️ Transfer tylko dla online players
- ⚠️ Wymaga low latency do Redis (<15ms)
- ⚠️ Niektóre entity nie synchronizowane
- ⚠️ MySQL opcjonalne (głównie Redis)

## 💡 Dlaczego Redis a nie Netty?

**Redis:**
- ✓ Built-in Pub/Sub
- ✓ Persistence (RDB/AOF)
- ✓ Proven scalability
- ✓ Simple JSON protocol
- ✓ Multiple data structures

**Netty:**
- ✗ Custom protocol needed
- ✗ Manual message routing
- ✗ Custom persistence
- ✗ More complexity

## 👨‍💻 Autor

**Mateusz (CzarnaWoda)**
- GitHub: [@CzarnaWoda](https://github.com/CzarnaWoda)
- Projekt: **JustPVP Network** (2018-2022)
- Peak: **2000+ concurrent players**

## 📚 Nauka z Projektu

**Key Learnings:**
- Redis Pub/Sub patterns
- Distributed systems architecture
- Cross-server synchronization
- BungeeCord + Spigot integration
- Event-driven design
- JSON serialization
- Packet-based communication

---

# SPIGOT-SECTORS - Packet Communication Flow

## Architektura 3-Warstwowa

```
┌─────────────────────────────────────────────────────┐
│                    LAYER 1: DTO                     │
│                 Packet Classes                       │
│  - Przechowują tylko dane                           │
│  - Brak logiki biznesowej                           │
│  - Implementują handlePacket() → delegacja          │
└─────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────┐
│                 LAYER 2: TRANSPORT                  │
│         Redis Pub/Sub + Gson Serialization          │
│  - PacketManager: packet ID registry                │
│  - SectorClientImpl: wysyłka przez Redis            │
│  - PacketListener: odbiór i deserializacja          │
└─────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────┐
│                 LAYER 3: HANDLER                    │
│              PacketHandlerImpl.java                 │
│  - Cała logika biznesowa                           │
│  - Interakcja z managers (Guild, User, etc.)       │
│  - Update stanu aplikacji                          │
└─────────────────────────────────────────────────────┘
```

## Przykład: Guild Chat Message

### 1. Packet (DTO) - tylko dane

```java
// pl/supereasy/sectors/api/packets/impl/guild/GuildMessagePacket.java
public class GuildMessagePacket extends Packet {
    private final String guildTag;
    private final String message;
    
    public GuildMessagePacket(String guildTag, String message) {
        this.guildTag = guildTag;
        this.message = message;
    }
    
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);  // tylko deleguje do handlera
    }
    
    // getters...
}
```

### 2. Wysyłka przez Redis

```java
// Kod w Guild.java
public void sendGuildMessage(final String msg, boolean betweenSectors) {
    if (betweenSectors) {
        final Packet packet = new GuildMessagePacket(this.guildTag, msg);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
    }
}

// SectorClientImpl.java
@Override
public void sendGlobalPacket(Packet packet) {
    final int packetID = plugin.getPacketManager().getPacketID(packet.getClass());
    // Format: "packetID@json"
    // Np: "42@{guildTag:'ABC',message:'Hello'}"
    plugin.getRedisManager()
          .getPubSub(PubSubType.PACKETS)
          .sendMessage(packetID + "@" + gson.toJson(packet));
}
```

### 3. Odbiór przez Redis Listener

```java
// PacketListener.java
public class PacketListener implements RedisListener<String> {
    
    public PacketListener(PubSubType type, RTopic redisTopic) {
        this.redisTopic = redisTopic;
        
        this.redisTopic.addListener(String.class, (channel, message) -> {
            // message = "42@{guildTag:'ABC',message:'Hello'}"
            
            String[] parts = message.split("@", 2);
            int packetID = Integer.parseInt(parts[0]);  // 42
            String json = parts[1];  // {guildTag:'ABC'...}
            
            // Get packet class by ID
            Class<? extends Packet> clazz = packetManager.getPacket(packetID);
            
            // Deserialize JSON → Packet object
            Packet packet = gson.fromJson(json, clazz);
            
            // Execute handler
            packet.handlePacket(packetHandler);
        });
    }
}
```

### 4. Handler (logika biznesowa)

```java
// PacketHandlerImpl.java
@Override
public void handle(GuildMessagePacket guildMessagePacket) {
    // 1. Znajdź gildię
    final Guild guild = this.plugin.getGuildManager()
                                   .getGuild(guildMessagePacket.getGuildTag());
    
    // 2. Jeśli gildia istnieje, wyślij wiadomość
    if (guild != null) {
        guild.sendGuildMessage(guildMessagePacket.getMessage());
    }
}

// Guild.java - sendGuildMessage (local)
public void sendGuildMessage(final String msg) {
    for (Player p : getOnlineMembersAsPlayers()) {
        ChatUtil.sendMessage(p, msg);
    }
}
```

## Kompletny Flow

```
═══════════════════════════════════════════════════════════════
                    GUILD CHAT FLOW
═══════════════════════════════════════════════════════════════

Player na Sector PVP-1: /g chat Hello everyone!
    ↓
ChatCommand → guild.sendGuildMessage("Hello everyone!", true)
    ↓
new GuildMessagePacket("ABC", "Hello everyone!")
    ↓
sectorClient.sendGlobalPacket(packet)
    ↓
PacketManager.getPacketID(GuildMessagePacket.class) → 42
    ↓
gson.toJson(packet) → {"guildTag":"ABC","message":"Hello everyone!"}
    ↓
Redis publish: "42@{...json...}"
    ↓
═══════════════════════════════════════════════════════════════
           BROADCAST DO WSZYSTKICH SEKTORÓW
═══════════════════════════════════════════════════════════════
    ↓
Sector PVP-1 (PacketListener):
    ↓ receive "42@{...}"
    ↓ deserialize → GuildMessagePacket
    ↓ packet.handlePacket(handler)
    ↓ handler.handle(packet)
    ↓ guild = guildManager.getGuild("ABC")
    ↓ guild.sendGuildMessage("Hello everyone!")
    ↓ Send to local online members of ABC
    
Sector PVP-2 (PacketListener):
    ↓ [same process]
    ↓ Send to local online members of ABC
    
Sector SPAWN (PacketListener):
    ↓ [same process]
    ↓ Send to local online members of ABC

═══════════════════════════════════════════════════════════════
Result: Wszyscy członkowie gildii ABC na WSZYSTKICH sektorach
        otrzymują wiadomość "Hello everyone!"
═══════════════════════════════════════════════════════════════
```

## Przykład z Prawdziwego Kodu

### GuildDestroyPacket Handler

```java
// Z PacketHandlerImpl.java (linia ~275)
@Override
public void handle(GuildDestroyPacket guildDestroyPacket) {
    // 1. Znajdź target guild
    final Guild targetGuild = this.plugin.getGuildManager()
                                         .getGuild(guildDestroyPacket.getTargetGuild());
    
    if (targetGuild != null) {
        // 2. Remove guild from all members
        for (UUID memberUUID : targetGuild.getMembers().keySet()) {
            final User user = this.plugin.getUserManager().getUser(memberUUID);
            user.setGuild(null);
            
            // 3. Update scoreboard if online
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                this.plugin.getTagManager().updateBoard(p);
            }
        }
        
        // 4. Delete guild from manager
        this.plugin.getGuildManager().deleteGuild(targetGuild);
        
        // 5. Broadcast message
        String msg = GuildConfig.INSTANCE.MESSAGES_GUILDDESTROY;
        msg = msg.replace("{GUILD}", guildDestroyPacket.getAttackerGuild());
        msg = msg.replace("{GUILDVICTIM}", targetGuild.getTag());
        Bukkit.broadcastMessage(ChatUtil.fixColor(msg));
    }
}
```

## Packet Types

### Global Packets (wszystkie sektory)

```java
sectorClient.sendGlobalPacket(packet);
// → Redis globalPacketTopic
// → PacketListener na każdym sektorze
```

**Przykłady:**
- `GuildMessagePacket` - guild chat
- `GuildDestroyPacket` - destroy guild
- `BroadcastChatMessage` - admin broadcast
- `TopPacket` - ranking updates

### Sector-Specific Packets (konkretny sektor)

```java
sectorClient.sendPacket(packet, "PVP-2");
// → Redis "PVP-2" topic
// → SectorPacketListener TYLKO na PVP-2
```

**Przykłady:**
- `PlayerTeleportPacket` - teleport gracza
- `MessageToUserPacket` - message do konkretnego gracza
- `PlayerSpawnTeleportPacket` - spawn teleport

### User Transfer Packets (player moving)

```java
sectorClient.sendUser(packet, "PVP-2");
// → Redis "PVP-2-user" topic
// → UserListener na PVP-2
```

**Przykład:**
- `UserChangeSectorPacket` - transfer gracza między sektorami

## PacketManager - Registry

```java
// PacketManager.java
private final Map<Integer, Class<? extends Packet>> packets = new HashMap<>();
private final Map<Class<? extends Packet>, Integer> packetIDs = new HashMap<>();

public void registerPacket(int id, Class<? extends Packet> clazz) {
    packets.put(id, clazz);
    packetIDs.put(clazz, id);
}

public int getPacketID(Class<? extends Packet> clazz) {
    return packetIDs.get(clazz);
}

public Class<? extends Packet> getPacket(int id) {
    return packets.get(id);
}
```

## Key Takeaways

1. **Pakiety = DTO** - tylko dane, zero logiki
2. **Handler = Logika** - cała business logic w PacketHandlerImpl
3. **Redis = Transport** - Pub/Sub messaging
4. **Gson = Serialization** - JSON format
5. **Separation of Concerns** - czysta architektura

---

⭐ **Jeśli projekt pomógł - zostaw gwiazdkę!** ⭐
