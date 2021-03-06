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
package nl.queuemanager.jms.impl;

import nl.queuemanager.jms.JMSBroker;
import nl.queuemanager.jms.JMSTopic;

import javax.jms.JMSException;

class JMSTopicImpl extends JMSDestinationImpl implements JMSTopic {

	protected final String name;
	
	public JMSTopicImpl(JMSBroker broker, String name) {
		super(broker);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public TYPE getType() {
		return TYPE.TOPIC;
	}

	public String getTopicName() throws JMSException {
		return getName();
	}

}
