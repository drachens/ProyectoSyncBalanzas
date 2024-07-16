package com.marsol.sync.service.transform;


public interface TransformationStrategy {

	void transformDataPLUs();
	void transformDataNotes();
	void transformDataLayouts();
}
