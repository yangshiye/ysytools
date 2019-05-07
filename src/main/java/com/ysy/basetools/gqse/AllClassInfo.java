package com.ysy.basetools.gqse;

/**
 * Created by gqzzd on 2018-5-28.
 */
public class AllClassInfo {
    private final ClassLoader classLoader;
    private final Class clazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllClassInfo that = (AllClassInfo) o;

        if (classLoader != null ? !classLoader.equals(that.classLoader) : that.classLoader != null) return false;
        return clazz != null ? clazz.equals(that.clazz) : that.clazz == null;
    }

    @Override
    public int hashCode() {
        int result = classLoader != null ? classLoader.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }

    public ClassLoader getClassLoader() {

        return classLoader;
    }

    public Class getClazz() {
        return clazz;
    }

    public AllClassInfo(Class clazz) {
        this.classLoader = clazz.getClassLoader();
        this.clazz = clazz;
    }

    public AllClassInfo(ClassLoader classLoader, Class clazz) {
        this.classLoader = classLoader;
        this.clazz = clazz;
    }
}
