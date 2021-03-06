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
import nl.queuemanager.jms.JMSQueue;
import nl.queuemanager.jms.JMSTopic;

public class DestinationFactory {
	private DestinationFactory(){}
	
	public static JMSQueue createQueue(JMSBroker broker, String name) {
		return new JMSQueueImpl(broker, name);
	}
	
	public static JMSTopic createTopic(JMSBroker broker, String name) {
		return new JMSTopicImpl(broker, name);
	}
	
}
