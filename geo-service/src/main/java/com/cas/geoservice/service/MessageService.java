/* (C)2023 */
package com.cas.geoservice.service;

public interface MessageService {

  void sendMessage(String exchangeName, String route, String message);
}
