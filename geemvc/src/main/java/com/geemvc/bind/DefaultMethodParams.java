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

package com.geemvc.bind;

import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.bind.param.ParamAdapterFactory;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.bind.param.TypedParamAdapter;
import com.geemvc.converter.*;
import com.geemvc.handler.RequestHandler;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultMethodParams implements MethodParams {
    protected final ParamAdapterFactory paramAdapterFactory;
    protected final ConverterAdapterFactory converterAdapterFactory;
    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultMethodParams(ParamAdapterFactory paramAdapterFactory, ConverterAdapterFactory converterAdapterFactory, ReflectionProvider reflectionProvider) {
        this.paramAdapterFactory = paramAdapterFactory;
        this.converterAdapterFactory = converterAdapterFactory;
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public List<MethodParam> get(RequestHandler requestHandler, RequestContext requestContext) {
        return requestHandler == null ? null : requestHandler.methodParams();
    }

    @Override
    public Map<String, List<String>> values(List<MethodParam> methodParams, RequestContext requestCtx) {
        Map<String, List<String>> paramValues = new LinkedHashMap<>();

        if (methodParams != null && !methodParams.isEmpty()) {
            for (MethodParam methodParam : methodParams) {
                Annotation paramAnnotation = methodParam.paramAnnotation();

                if (paramAnnotation != null) {
                    ParamAdapter<Annotation> paramAdapter = paramAdapterFactory.create(paramAnnotation.annotationType());

                    ParamContext paramCtx = injector.getInstance(ParamContext.class).build(methodParam, paramValues, null, requestCtx);

                    String name = name(paramAdapter, paramAnnotation, methodParam);

                    paramValues.put(name, paramAdapter.getValue(paramAnnotation, name, paramCtx));
                }
            }
        }

        return paramValues;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Map<String, Object> typedValues(Map<String, List<String>> requestValues, List<MethodParam> methodParams, RequestContext requestCtx) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);
        BeanConverter beanConverter = injector.getInstance(BeanConverter.class);

        Map<String, Object> typedValues = new LinkedHashMap<>();

        if (methodParams != null && !methodParams.isEmpty()) {
            for (MethodParam methodParam : methodParams) {
                ParamContext paramCtx = injector.getInstance(ParamContext.class).build(methodParam, requestValues, typedValues, requestCtx);
                Annotation paramAnnotation = methodParam.paramAnnotation();

                // Find adapter class for the method parameter annotation.
                ParamAdapter<Annotation> paramAdapter = paramAdapterFactory.create(paramAnnotation.annotationType());

                String name = name(paramAdapter, paramAnnotation, methodParam);

                // If the ParamAdapter is already returning a specific type, we will skip type-conversion and just add
                // the value directly to the typedValues map.
                if (paramAdapter instanceof TypedParamAdapter) {
                    Object typedValue = ((TypedParamAdapter) paramAdapter).getTypedValue(paramAnnotation, name, paramCtx);
                    typedValues.put(name, typedValue);
                    continue;
                }

                List<String> value = requestValues.get(name);
                Class<?> type = methodParam.type();

                // No value in request found to convert.
                if (value == null) {
                    // If parameter is a bean and @Nullable is not set, we create a new empty instance.
                    if (!methodParam.isNullable() && !simpleConverter.canConvert((Class<?>) type)) {
                        typedValues.put(name, beanConverter.newInstance(type));
                    } else {
                        // Otherwise we simply pass a null value to the handler.
                        typedValues.put(name, null);
                    }

                    continue;
                } else if (value.size() == 0) {
                    typedValues.put(name, null);
                    continue;
                }

                Type parameterizedType = methodParam.parameterizedType();
                List<Class<?>> genericType = reflectionProvider.getGenericType(parameterizedType);

                ConverterAdapter<?> converterAdapter = converterAdapterFactory.create(type, parameterizedType);

                if (converterAdapter != null) {
                    Object convertedValue = converterAdapter.fromStrings(value, injector.getInstance(ConverterContext.class).build(name, type, genericType, requestCtx));
                    typedValues.put(name, convertedValue);
                } else {
                    if (simpleConverter.canConvert((Class<?>) type)) {
                        Object typedVal = simpleConverter.fromString(value.get(0), (Class<?>) type);
                        typedValues.put(name, typedVal);
                    } else {
                        Object bean = beanConverter.fromStrings(value, name, type);
                        typedValues.put(name, bean);
                    }
                }
            }
        }

        return typedValues;
    }

    @Override
    public String name(ParamAdapter<Annotation> paramAdapter, Annotation paramAnnotation, MethodParam methodParam) {
        String annotationName = paramAdapter.getName(paramAnnotation);
        return Str.isEmpty(annotationName) ? methodParam.name() : annotationName;
    }
}