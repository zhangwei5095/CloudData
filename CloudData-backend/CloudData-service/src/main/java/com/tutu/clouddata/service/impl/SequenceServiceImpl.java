package com.tutu.clouddata.service.impl;

import javax.annotation.Resource;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tutu.clouddata.api.SequenceService;
import com.tutu.clouddata.auth.dao.SystemDatastore;
import com.tutu.clouddata.dto.AutoIncrementEntity;

@Service("sequenceService")
public class SequenceServiceImpl implements SequenceService {
	@Resource
	private SystemDatastore systemDatastore;

	@Override
	public Long getNextId(final String seq_name, final long minimumValue) {
		final Query<AutoIncrementEntity> query = systemDatastore
				.find(AutoIncrementEntity.class).field("_id").equal(seq_name);
		final UpdateOperations<AutoIncrementEntity> update = systemDatastore
				.createUpdateOperations(AutoIncrementEntity.class).inc("value");
		AutoIncrementEntity autoIncrement = systemDatastore.findAndModify(
				query, update);

		if (autoIncrement == null) {
			autoIncrement = new AutoIncrementEntity(seq_name, minimumValue);
			systemDatastore.save(autoIncrement);
		}
		return autoIncrement.getValue();
	}

}
