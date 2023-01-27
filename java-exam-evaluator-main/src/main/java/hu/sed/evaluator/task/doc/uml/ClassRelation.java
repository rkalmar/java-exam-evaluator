package hu.sed.evaluator.task.doc.uml;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum ClassRelation {
    Association("--"),
    Specialization(" <|-- "),
    Aggregation("o--"),
    Composition("*--");

    final String sign;
}