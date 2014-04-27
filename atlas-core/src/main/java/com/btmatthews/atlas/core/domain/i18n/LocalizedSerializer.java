/*
 * Copyright 2011-2013 Brian Thomas Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.atlas.core.domain.i18n;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.1
 */
public class LocalizedSerializer extends JsonSerializer<Localized> {
    @Override
    public void serialize(final Localized value,
                          final JsonGenerator generator,
                          final SerializerProvider provider) throws
            IOException {
        generator.writeStartObject();
        value.getValues().forEach((itemKey, itemValue) -> {
            try {
                generator.writeStringField(
                        ((Locale) itemKey).toLanguageTag(),
                        (String) itemValue);
            } catch (final IOException e) {
            }
        });
        generator.writeEndObject();

    }
}
