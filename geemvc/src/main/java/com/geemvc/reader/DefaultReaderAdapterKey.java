/*
 * Copyright 2016 the original author or authors.
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

package com.geemvc.reader;

import java.util.List;

public class DefaultReaderAdapterKey implements ReaderAdapterKey {
    protected List<Class<?>> genericTypes = null;
    protected Class<?> type = null;
    protected int weight = 0;
    protected boolean isInitialized = false;

    @Override
    public ReaderAdapterKey build(Class<?> type, List<Class<?>> genericTypes) {
        if (!isInitialized) {
            this.type = type;
            this.genericTypes = genericTypes;
            this.isInitialized = true;
        } else {
            throw new IllegalStateException("ReaderAdapterKey.build() can only be called once");
        }

        return this;
    }

    @Override
    public ReaderAdapterKey build(Class<?> type) {
        if (!isInitialized) {
            this.type = type;
            this.isInitialized = true;
        } else {
            throw new IllegalStateException("ReaderAdapterKey.build() can only be called once");
        }

        return this;
    }

    @Override
    public ReaderAdapterKey weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    @Override
    public List<Class<?>> genericTypes() {
        return genericTypes;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((genericTypes == null) ? 0 : genericTypes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultReaderAdapterKey other = (DefaultReaderAdapterKey) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (genericTypes == null) {
            if (other.genericTypes != null)
                return false;
        } else if (!genericTypes.equals(other.genericTypes))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DefaultReaderAdapterKey [genericTypes=" + genericTypes + ", type=" + type + ", weight=" + weight + "]";
    }
}
