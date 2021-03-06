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

package com.geemvc.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.geemvc.Bindings;
import com.geemvc.RequestContext;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;
import com.geemvc.view.bean.Result;

/**
 * Created by Michael on 13.07.2016.
 */
public interface LifecycleContext {
    LifecycleContext build(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices);

    LifecycleContext lifecycle(Annotation lifecycleAnnotation);

    Annotation lifecycle();

    RequestHandler requestHandler();

    Errors errors();

    Notices notices();

    Bindings bindings();

    LifecycleContext bindings(Bindings bindings);

    Result result();

    LifecycleContext result(Result result);

    boolean isInvokeHandler();

    LifecycleContext invokeHandler(boolean invokeHandler);

    RequestContext requestCtx();

    Class<?> controllerClass();

    Method handlerMethod();
}
