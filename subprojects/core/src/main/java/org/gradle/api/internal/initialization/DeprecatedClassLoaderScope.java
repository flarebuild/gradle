/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.initialization;

import org.gradle.api.internal.initialization.loadercache.ClassLoaderCache;
import org.gradle.internal.classpath.ClassPath;

public class DeprecatedClassLoaderScope extends DefaultClassLoaderScope {

    private ClassPath deprecatedClasspath;
    private ClassLoader deprecatedExportClassloader;
    private ClassLoader deprecatedLocalClassloader;

    public DeprecatedClassLoaderScope(ClassLoaderScopeIdentifier id, ClassLoaderScope parent, ClassLoaderCache classLoaderCache, ClassPath deprecatedClasspath) {
        super(id, parent, classLoaderCache);
        this.deprecatedClasspath = deprecatedClasspath;
    }

    @Override
    public ClassLoaderScope export(ClassPath classPath) {
        export = export.plus(classPath);
        return this;
    }

    @Override
    public ClassLoaderScope deprecated() {
        return this;
    }

    @Override
    public ClassLoader getExportClassLoader() {
        if (deprecatedExportClassloader == null) {
            deprecatedExportClassloader = new DefaultDeprecatedClassLoader(buildLockedLoader(id.exportId(), deprecatedClasspath), super.getExportClassLoader());
        }
        return deprecatedExportClassloader;
    }

    @Override
    public ClassLoader getLocalClassLoader() {
        if (deprecatedLocalClassloader == null) {
            deprecatedLocalClassloader = new DefaultDeprecatedClassLoader(buildLockedLoader(id.localId(), deprecatedClasspath), super.getLocalClassLoader());
        }
        return deprecatedLocalClassloader;
    }

    @Override
    public ClassLoaderScope createChild(String name) {
        return new DeprecatedClassLoaderScope(id.child(name), this, classLoaderCache, deprecatedClasspath);
//
//        super.createChild()
//        DeprecatedClassLoaderScope deprecatedScope = new DeprecatedClassLoaderScope(id.child("deprecated"), parent, classLoaderCache, export.plus(local));
//        if (isLocked()) {
//            deprecatedScope.lock();
//        }
//        return deprecatedScope;

    }
}