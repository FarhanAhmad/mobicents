/*
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
package javax.servlet.sip;
/**
 * Implementations of this interface are notified of changes to the list of active SipSessions in a SIP servlet application. To recieve notification events, the implementation class must be configured in the deployment descriptor for the SIP application.
 */
public interface SipSessionListener extends java.util.EventListener{
    /**
     * Notification that a SipSession was created.
     */
    void sessionCreated(javax.servlet.sip.SipSessionEvent se);

    /**
     * Notification that a SipSession was destroyed.
     */
    void sessionDestroyed(javax.servlet.sip.SipSessionEvent se);

}
