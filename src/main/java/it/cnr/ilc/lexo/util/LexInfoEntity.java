/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.util.EnumSet;

/**
 *
 * @author andreabellandi
 */
public class LexInfoEntity {

    public static boolean containsString(Class<? extends Enum> enumClass, String string) {
        String[] stringValues = stringValues(enumClass);
        for (int i = 0; i < stringValues.length; i++) {
            if (stringValues[i].equals(string)) {
                return true;
            }
        }
        return false;
    }

    public static String[] stringValues(Class<? extends Enum> enumClass) {
        EnumSet enumSet = EnumSet.allOf(enumClass);
        String[] values = new String[enumSet.size()];
        int i = 0;
        for (Object enumValue : enumSet) {
            values[i] = String.valueOf(enumValue);
            i++;
        }
        return values;
    }

    public static enum LexicalEntryPoS {
        adjective("adjective"),
        adjective_i("adjective-i"),
        adjective_na("adjective-na"),
        adposition("adposition"),
        adverb("adverb"),
        adverbialPronoun("adverbialPronoun"),
        affirmativeParticle("affirmativeParticle"),
        affixedPersonalPronoun("affixedPersonalPronoun"),
        allusivePronoun("allusivePronoun"),
        article("article"),
        auxiliary("auxiliary"),
        bullet("bullet"),
        cardinalNumeral("cardinalNumeral"),
        circumposition("circumposition"),
        closeParenthesis("closeParenthesis"),
        collectivePronoun("collectivePronoun"),
        colon("colon"),
        comma("comma"),
        commonNoun("commonNoun"),
        comparativeParticle("comparativeParticle"),
        compoundPreposition("compoundPreposition"),
        conditionalParticle("conditionalParticle"),
        conditionalPronoun("conditionalPronoun"),
        conjuction("conjuction"),
        conjunction("conjunction"),
        coordinatingConjunction("coordinatingConjunction"),
        coordinationParticle("coordinationParticle"),
        copula("copula"),
        deficientVerb("deficientVerb"),
        definiteArticle("definiteArticle"),
        demonstrativeDeterminer("demonstrativeDeterminer"),
        demonstrativePronoun("demonstrativePronoun"),
        determiner("determiner"),
        diminutiveNoun("diminutiveNoun"),
        distinctiveParticle("distinctiveParticle"),
        emphaticPronoun("emphaticPronoun"),
        exclamativeDeterminer("exclamativeDeterminer"),
        exclamativePoint("exclamativePoint"),
        exclamativePronoun("exclamativePronoun"),
        existentialPronoun("existentialPronoun"),
        fusedPreposition("fusedPreposition"),
        fusedPrepositionDeterminer("fusedPrepositionDeterminer"),
        fusedPrepositionPronoun("fusedPrepositionPronoun"),
        fusedPronounAuxiliary("fusedPronounAuxiliary"),
        futureParticle("futureParticle"),
        generalAdverb("generalAdverb"),
        generalizationWord("generalizationWord"),
        genericNumeral("genericNumeral"),
        impersonalPronoun("impersonalPronoun"),
        indefiniteArticle("indefiniteArticle"),
        indefiniteCardinalNumeral("indefiniteCardinalNumeral"),
        indefiniteDeterminer("indefiniteDeterminer"),
        indefiniteMultiplicativeNumeral("indefiniteMultiplicativeNumeral"),
        indefiniteOrdinalNumeral("indefiniteOrdinalNumeral"),
        indefinitePronoun("indefinitePronoun"),
        infinitiveParticle("infinitiveParticle"),
        interjection("interjection"),
        interrogativeCardinalNumeral("interrogativeCardinalNumeral"),
        interrogativeDeterminer("interrogativeDeterminer"),
        interrogativeMultiplicativeNumeral("interrogativeMultiplicativeNumeral"),
        interrogativeOrdinalNumeral("interrogativeOrdinalNumeral"),
        interrogativeParticle("interrogativeParticle"),
        interrogativePronoun("interrogativePronoun"),
        interrogativeRelativePronoun("interrogativeRelativePronoun"),
        invertedComma("invertedComma"),
        irreflexivePersonalPronoun("irreflexivePersonalPronoun"),
        letter("letter"),
        lightVerb("lightVerb"),
        mainVerb("mainVerb"),
        modal("modal"),
        multiplicativeNumeral("multiplicativeNumeral"),
        negativeParticle("negativeParticle"),
        negativePronoun("negativePronoun"),
        noun("noun"),
        numeral("numeral"),
        numeralDeterminer("numeralDeterminer"),
        numeralFraction("numeralFraction"),
        numeralPronoun("numeralPronoun"),
        openParenthesis("openParenthesis"),
        ordinalAdjective("ordinalAdjective"),
        participleAdjective("participleAdjective"),
        particle("particle"),
        partitiveArticle("partitiveArticle"),
        pastParticipleAdjective("pastParticipleAdjective"),
        personalPronoun("personalPronoun"),
        plainVerb("plainVerb"),
        point("point"),
        possessiveAdjective("possessiveAdjective"),
        possessiveDeterminer("possessiveDeterminer"),
        possessiveParticle("possessiveParticle"),
        possessivePronoun("possessivePronoun"),
        possessiveRelativePronoun("possessiveRelativePronoun"),
        postposition("postposition"),
        preposition("preposition"),
        prepositionalAdverb("prepositionalAdverb"),
        presentParticipleAdjective("presentParticipleAdjective"),
        presentativePronoun("presentativePronoun"),
        pronominalAdverb("pronominalAdverb"),
        pronoun("pronoun"),
        properNoun("properNoun"),
        punctuation("punctuation"),
        qualifierAdjective("qualifierAdjective"),
        questionMark("questionMark"),
        reciprocalPronoun("reciprocalPronoun"),
        reflexiveDeterminer("reflexiveDeterminer"),
        reflexivePersonalPronoun("reflexivePersonalPronoun"),
        reflexivePossessivePronoun("reflexivePossessivePronoun"),
        relationNoun("relationNoun"),
        relativeDeterminer("relativeDeterminer"),
        relativeParticle("relativeParticle"),
        relativePronoun("relativePronoun"),
        semiColon("semiColon"),
        slash("slash"),
        strongPersonalPronoun("strongPersonalPronoun"),
        subordinatingConjunction("subordinatingConjunction"),
        superlativeParticle("superlativeParticle"),
        suspensionPoints("suspensionPoints"),
        symbol("symbol"),
        unclassifiedParticle("unclassifiedParticle"),
        verb("verb"),
        weakPersonalPronoun("weakPersonalPronoun");

        private final String pos;

        private LexicalEntryPoS(String pos) {
            this.pos = pos;
        }

        @Override
        public String toString() {
            return this.pos;
        }
    }

}
