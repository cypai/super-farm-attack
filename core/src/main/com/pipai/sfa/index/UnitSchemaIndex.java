package com.pipai.sfa.index;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableList;
import com.pipai.sfa.battle.domain.UnitSchema;

public class UnitSchemaIndex {

	private HashMap<String, UnitSchema> unitIndex;

	public UnitSchemaIndex(FileHandle cropFile) {
		unitIndex = new HashMap<>();

		String rawData = cropFile.readString("UTF-8");
		try {
			CSVParser parser = CSVParser.parse(rawData, CSVFormat.DEFAULT.withHeader());
			for (CSVRecord record : parser.getRecords()) {
				String name = record.get("name");
				unitIndex.put(name, new UnitSchema(name,
						Integer.valueOf(record.get("hp")),
						Integer.valueOf(record.get("pAtk")),
						Integer.valueOf(record.get("pDef")),
						Integer.valueOf(record.get("speed"))));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ImmutableList<UnitSchema> getAllUnitSchemas() {
		return ImmutableList.copyOf(unitIndex.values());
	}

}
