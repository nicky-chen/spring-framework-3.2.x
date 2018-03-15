//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.springframework.asm.ClassReader;

public abstract class AbstractClassGenerator implements ClassGenerator {
    private static final Object NAME_KEY = new Object();
    private static final ThreadLocal CURRENT = new ThreadLocal();
    private GeneratorStrategy strategy;
    private NamingPolicy namingPolicy;
    private AbstractClassGenerator.Source source;
    private ClassLoader classLoader;
    private String namePrefix;
    private Object key;
    private boolean useCache;
    private String className;
    private boolean attemptLoad;

    protected AbstractClassGenerator(AbstractClassGenerator.Source source) {
        this.strategy = DefaultGeneratorStrategy.INSTANCE;
        this.namingPolicy = DefaultNamingPolicy.INSTANCE;
        this.useCache = true;
        this.source = source;
    }

    protected void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    protected final String getClassName() {
        if(this.className == null) {
            this.className = this.getClassName(this.getClassLoader());
        }

        return this.className;
    }

    private String getClassName(ClassLoader loader) {
        final Set nameCache = this.getClassNameCache(loader);
        return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, new Predicate() {
            public boolean evaluate(Object arg) {
                return nameCache.contains(arg);
            }
        });
    }

    private Set getClassNameCache(ClassLoader loader) {
        return (Set)((Map)this.source.cache.get(loader)).get(NAME_KEY);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setNamingPolicy(NamingPolicy namingPolicy) {
        if(namingPolicy == null) {
            namingPolicy = DefaultNamingPolicy.INSTANCE;
        }

        this.namingPolicy = (NamingPolicy)namingPolicy;
    }

    public NamingPolicy getNamingPolicy() {
        return this.namingPolicy;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public boolean getUseCache() {
        return this.useCache;
    }

    public void setAttemptLoad(boolean attemptLoad) {
        this.attemptLoad = attemptLoad;
    }

    public boolean getAttemptLoad() {
        return this.attemptLoad;
    }

    public void setStrategy(GeneratorStrategy strategy) {
        if(strategy == null) {
            strategy = DefaultGeneratorStrategy.INSTANCE;
        }

        this.strategy = (GeneratorStrategy)strategy;
    }

    public GeneratorStrategy getStrategy() {
        return this.strategy;
    }

    public static AbstractClassGenerator getCurrent() {
        return (AbstractClassGenerator)CURRENT.get();
    }

    public ClassLoader getClassLoader() {
        ClassLoader t = this.classLoader;
        if(t == null) {
            t = this.getDefaultClassLoader();
        }

        if(t == null) {
            t = this.getClass().getClassLoader();
        }

        if(t == null) {
            t = Thread.currentThread().getContextClassLoader();
        }

        if(t == null) {
            throw new IllegalStateException("Cannot determine classloader");
        } else {
            return t;
        }
    }

    protected abstract ClassLoader getDefaultClassLoader();

    protected Object create(Object key) {
        try {
            Class gen = null;
            AbstractClassGenerator.Source var3 = this.source;
            synchronized(this.source) {
                ClassLoader loader = this.getClassLoader();
                Map cache2 = null;
                cache2 = (Map)this.source.cache.get(loader);
                if(cache2 == null) {
                    cache2 = new HashMap();
                    ((Map)cache2).put(NAME_KEY, new HashSet());
                    this.source.cache.put(loader, cache2);
                } else if(this.useCache) {
                    Reference ref = (Reference)((Map)cache2).get(key);
                    gen = (Class)(ref == null?null:ref.get());
                }

                if(gen == null) {
                    Object save = CURRENT.get();
                    CURRENT.set(this);

                    Object var24;
                    try {
                        this.key = key;
                        if(this.attemptLoad) {
                            try {
                                gen = loader.loadClass(this.getClassName());
                            } catch (ClassNotFoundException var17) {
                                ;
                            }
                        }

                        if(gen == null) {
                            byte[] b = this.strategy.generate(this);
                            String className = ClassNameReader.getClassName(new ClassReader(b));
                            this.getClassNameCache(loader).add(className);
                            gen = ReflectUtils.defineClass(className, b, loader);
                        }

                        if(this.useCache) {
                            ((Map)cache2).put(key, new WeakReference(gen));
                        }

                        var24 = this.firstInstance(gen);
                    } finally {
                        CURRENT.set(save);
                    }

                    return var24;
                }
            }

            return this.firstInstance(gen);
        } catch (RuntimeException var20) {
            throw var20;
        } catch (Error var21) {
            throw var21;
        } catch (Exception var22) {
            throw new CodeGenerationException(var22);
        }
    }

    protected abstract Object firstInstance(Class var1) throws Exception;

    protected abstract Object nextInstance(Object var1) throws Exception;

    protected static class Source {
        String name;
        Map cache = new WeakHashMap();

        public Source(String name) {
            this.name = name;
        }
    }
}
