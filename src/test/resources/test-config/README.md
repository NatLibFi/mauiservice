This directory contains a minimal test configuration for MauiService.

# Vocabulary

The vocabulary is a small subset of 130 concepts related to archaeology that
have been extracted from the General Finnish Ontology YSO. Only the
information relevant for Maui is included (i.e. preferred and alternate
labels, hierarchy and related concepts). 

The SPARQL CONSTRUCT query used for extracting the subset is included in the
file `extract-yso-archaeology.rq`.

YSO is copyrighted by National Library of Finland, Semantic Computing
Research Group (SeCo) and The Finnish Terminology Centre TSK. It is used
here according to the CC By 4.0 license.

# Training documents

The `train` subdirectory contains 28 documents with associated keywords
drawn from YSO. These are questions related to archaeology picked from the
[Ask a librarian](https://www.kirjastot.fi/kysy) service. The same set of
example documents is also used in the [unit tests of the Annif
tool](https://github.com/NatLibFi/Annif/tree/master/tests/corpora/archaeology).

# Model

The model `yso-archaeology.model` is built from the above described
vocabulary and training documents using MauiModelBuilder.
