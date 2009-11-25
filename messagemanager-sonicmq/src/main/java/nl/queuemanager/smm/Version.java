/**
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
package nl.queuemanager.smm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class Version {
	
	private static String version;
	
	public static synchronized String getVersion() {		
		if(version == null) {
			version = "unknown version";
			
			for(URL u: ((URLClassLoader)Version.class.getClassLoader()).getURLs()) {
				System.out.println(u);
			}
			
			try {
				InputStream stream = Version.class.getClassLoader().getResourceAsStream(
						"META-INF/maven/nl.queuemanager/messagemanager-sonicmq/pom.properties");
				if(stream != null) {
					Properties p = new Properties();
					p.load(stream);
					version = p.getProperty("version");
					stream.close();
				} else {
					System.out.println("Unable to read version information file");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return version;
	}

}
