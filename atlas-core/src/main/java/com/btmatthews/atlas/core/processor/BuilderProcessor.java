package com.btmatthews.atlas.core.processor;

import com.btmatthews.atlas.core.annotation.BuilderFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.btmatthews.atlas.core.annotation.BuilderFactory")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {
        for (final Element elem : roundEnv.getElementsAnnotatedWith(BuilderFactory.class)) {
            final BuilderFactory annotation = elem.getAnnotation(BuilderFactory.class);
            final Class<?> implementationClass = annotation.value();
        }
        return true;
    }
}
