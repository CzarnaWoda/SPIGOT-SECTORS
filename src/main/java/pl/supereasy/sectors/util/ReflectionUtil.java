package pl.supereasy.sectors.util;


import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static Class getOBCClass(String name) {
        Class c = null;

        try {
            c = Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return c;
    }

    public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        try {

            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class getCraftClass(String name) {
        String className = "net.minecraft.server." + getVersion() + name;
        Class c = null;

        try {
            c = Class.forName(className);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return c;
    }

    public static Class getBukkitClass(String name) {
        String className = "org.bukkit.craftbukkit." + getVersion() + name;
        Class c = null;

        try {
            c = Class.forName(className);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return c;
    }

    public static Class getNMSClass(String name) {
        Class c = null;

        try {
            c = Class.forName("net.minecraft.server." + version + "." + name);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return c;
    }

    public static void setValue(Field f, Object o, Object v) {
        try {
            f.setAccessible(true);
            f.set(o, v);
            f.setAccessible(false);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static Object getHandle(Entity entity) {
        try {
            return getMethod(entity.getClass(), "getHandle").invoke(entity);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Object getHandle(World world) {
        try {
            return getMethod(world.getClass(), "getHandle").invoke(world);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class cl, String field_name) {
        try {
            return cl.getDeclaredField(field_name);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }


    public static Field getPrivateField(Class cl, String field_name) {
        try {
            Field e = cl.getDeclaredField(field_name);
            e.setAccessible(true);
            return e;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static Object getValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Method getMethod(Class cl, String method, Class... args) {
        Method[] methods;
        int length = (methods = cl.getMethods()).length;

        for (int i = 0; i < length; ++i) {
            Method m = methods[i];
            if (m.getName().equals(method) && classListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }

        return null;
    }

    public static Method getMethod(Class cl, String method) {
        Method[] methods;
        int length = (methods = cl.getMethods()).length;

        for (int i = 0; i < length; ++i) {
            Method m = methods[i];
            if (m.getName().equals(method)) {
                return m;
            }
        }

        return null;
    }

    private static boolean classListEqual(Class[] l1, Class[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        } else {
            for (int i = 0; i < l1.length; ++i) {
                if (l1[i] != l2[i]) {
                    equal = false;
                    break;
                }
            }

            return equal;
        }
    }

    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf(46) + 1) + ".";
        return version;
    }


    public static String getReflectionString() {
        return "net.minecraft.server.g.";
    }
}