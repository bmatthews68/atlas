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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Thomas Matthews</a>
 * @since 1.0.1
 */
public class LocalizedDeserializer extends JsonDeserializer<Localized> {
    @Override
    public Localized deserialize(final JsonParser parser,
                                         final DeserializationContext context)
            throws IOException {
        final LocalizedBuilder<Object> builder = new LocalizedBuilder<>();
        final ObjectCodec codec = parser.getCodec();
        final JsonNode node = codec.readTree(parser);
        final Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iterator.next();
            builder.setValue(
                    Locale.forLanguageTag(entry.getKey()),
                    entry.getValue().textValue());
        }
        return builder.build();
    }
}
