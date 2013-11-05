package com.xeiam.xchange.historical.service.polling;

interface LineFactory<T> {
	T instantiateFromLine(String line);
}
