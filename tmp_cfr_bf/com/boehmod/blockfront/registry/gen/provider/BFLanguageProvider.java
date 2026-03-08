/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen.provider;

import com.boehmod.blockfront.registry.gen.CrateGenerator;
import com.boehmod.blockfront.registry.gen.MultiformGenerator;
import com.boehmod.blockfront.registry.gen.PlanksGenerator;
import com.boehmod.blockfront.registry.gen.ShuttersGenerator;
import com.boehmod.blockfront.registry.gen.WoodTypeGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class BFLanguageProvider
extends LanguageProvider {
    public BFLanguageProvider(PackOutput packOutput) {
        super(packOutput, "bf_gen", "en_us");
    }

    protected void addTranslations() {
        for (Object object : MultiformGenerator.INSTANCES) {
            ((MultiformGenerator)object).provideLanguage(this);
        }
        for (Object object : CrateGenerator.INSTANCES) {
            ((CrateGenerator)object).provideLanguage(this);
        }
        for (Object object : PlanksGenerator.INSTANCES) {
            ((PlanksGenerator)object).provideLanguage(this);
        }
        for (Object object : ShuttersGenerator.INSTANCES) {
            ((ShuttersGenerator)object).provideLanguage(this);
        }
        for (Object object : WoodTypeGenerator.INSTANCES) {
            ((WoodTypeGenerator)object).provideLanguage(this);
        }
    }
}

