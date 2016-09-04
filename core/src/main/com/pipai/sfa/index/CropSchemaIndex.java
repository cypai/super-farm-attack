package com.pipai.sfa.index;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableList;
import com.pipai.sfa.battle.domain.CropSchema;

public class CropSchemaIndex {

	private HashMap<String, CropSchema> cropIndex;

	public CropSchemaIndex(FileHandle cropFile) {
		cropIndex = new HashMap<>();

		String rawData = cropFile.readString("UTF-8");
		try {
			CSVParser parser = CSVParser.parse(rawData, CSVFormat.DEFAULT.withHeader());
			for (CSVRecord record : parser.getRecords()) {
				String name = record.get("name");
				cropIndex.put(name, new CropSchema(name,
						Integer.valueOf(record.get("hp")),
						Integer.valueOf(record.get("pAtk")),
						Integer.valueOf(record.get("pDef")),
						Integer.valueOf(record.get("yieldTime")),
						Integer.valueOf(record.get("mass"))));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ImmutableList<CropSchema> getAllCropSchemas() {
		return ImmutableList.copyOf(cropIndex.values());
	}

}
