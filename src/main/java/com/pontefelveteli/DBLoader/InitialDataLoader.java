package com.pontefelveteli.DBLoader;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;



@Component
public class InitialDataLoader {

    @Autowired
    private DatabaseUploader databaseUploader;

    @PostConstruct
    public void init() {
        // Adatbázis feltöltése az alkalmazás indulásakor
        databaseUploader.uploadData();
    }
}

