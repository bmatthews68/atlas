/*
 * Copyright 2011 Brian Matthews
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

package com.btmatthews.atlas.core.id.uuid;

import java.util.UUID;

import com.btmatthews.atlas.core.id.IdentifierGenerator;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class UUIDIdentifierGenerator implements IdentifierGenerator<String> {

	private TimeBasedGenerator generator;

	public UUIDIdentifierGenerator() {
		final EthernetAddress address = EthernetAddress.fromInterface();
		generator = Generators.timeBasedGenerator(address);
	}

	public String generate() {
		final UUID uuid = this.generator.generate();
		return uuid.toString();
	}
}
