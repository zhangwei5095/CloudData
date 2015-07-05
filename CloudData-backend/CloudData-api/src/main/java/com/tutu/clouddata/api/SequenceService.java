package com.tutu.clouddata.api;

public interface SequenceService {
	Long getNextId(String seq_name, long minimumValue);
}
