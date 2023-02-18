package hu.sed.evaluator.task.doc.uml;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum ClassRelation {
    Association("--"),
    Specialization("<|--"),
    Implementation("<|.."),
    Aggregation("o--"),
    Composition("*--"),
    ThrowsException("..");

    final String sign;
}