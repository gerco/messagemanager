/**

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
package nl.queuemanager.core.jms.impl;

import nl.queuemanager.core.jms.JMSBroker;

abstract class JMSDestinationImpl implements nl.queuemanager.core.jms.JMSDestination {

	protected JMSBroker broker;
	
	public JMSBroker getBroker() {
		return broker;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof nl.queuemanager.core.jms.JMSDestination) 
			&& ((nl.queuemanager.core.jms.JMSDestination)o).getType().equals(getType())
			&& ((nl.queuemanager.core.jms.JMSDestination)o).getName().equals(getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	public int compareTo(nl.queuemanager.core.jms.JMSDestination o) {
		return toString().toLowerCase().compareTo(o.toString().toLowerCase());
	}
}
