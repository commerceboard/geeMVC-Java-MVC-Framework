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

package com.geemvc.config;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.geemvc.inject.InjectorProvider;

public interface Configuration {
    static final String VIEW_PREFIX_KEY = "view-prefix";

    static final String VIEW_SUFFIX_KEY = "view-suffix";

    static final String SUPPORTED_LOCALES_KEY = "supported-locales";

    static final String DEFAULT_CHARACTER_ENCODING_KEY = "default-character-encoding";

    static final String DEFAULT_CONTENT_TYPE_KEY = "default-content-type";

    static final String INJECTOR_PROVIDER_KEY = "injector-provider";

    static final String REFLECTIONS_PROVIDER_KEY = "reflections-provider";

    static final String REFLECTIONS_INCLUDE_LIBS_KEY = "reflections-include-libs";

    static final String REFLECTIONS_EXCLUDE_LIBS_KEY = "reflections-exclude-libs";

    static final String EXCLUDE_PATH_MAPPING_KEY = "exclude-path-mapping";

    static final String SUPPORTED_URI_SUFFIXES_KEY = "supported-uri-suffixes";

    static final String JAX_RS_ENABLED_KEY = "jax-rs-enabled";

    Configuration build(Map<String, String> configurationMap);

    String viewPrefix();

    String viewSuffix();

    String defaultCharacterEncoding();

    String defaultContentType();

    Set<Locale> supportedLocales();

    String characterEncodingFor(Locale locale);

    InjectorProvider injectorProvider();

    Set<String> excludePathMappinig();

    List<String> supportedUriSuffixes();

    List<String> reflectionsLibIncludes();

    List<String> reflectionsLibExcludes();

    boolean isJaxRsEnabled();
}
