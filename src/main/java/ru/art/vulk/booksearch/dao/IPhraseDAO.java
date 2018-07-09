package ru.art.vulk.booksearch.dao;

import ru.art.vulk.booksearch.praseEntity.PhraseEntity;

import java.util.List;

public interface IPhraseDAO {

    List<PhraseEntity> getAllPhrases();
    void addPhrase(List<PhraseEntity> publication);
}
