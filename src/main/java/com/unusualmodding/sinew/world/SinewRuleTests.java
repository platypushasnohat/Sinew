package com.unusualmodding.sinew.world;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.world.structure.rule_test.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SinewRuleTests {
    public static final DeferredRegister<RuleTestType<?>> RULE_TESTS = DeferredRegister.create(Registries.RULE_TEST, Sinew.MODID);
    public static final RegistryObject<RuleTestType<BiomeRuleTest>> BIOME_RULE_TEST = RULE_TESTS.register("biome_rule_test", () -> () -> BiomeRuleTest.CODEC);

    public static final RegistryObject<RuleTestType<DimensionRuleTest>> DIMENSION_RULE_TEST = RULE_TESTS.register("dimension_rule_test", () -> () -> DimensionRuleTest.CODEC);
    public static final RegistryObject<RuleTestType<ChanceRuleTest>> CHANCE_RULE_TEST = RULE_TESTS.register("chance_rule_test", () -> () -> ChanceRuleTest.CODEC);
    public static final RegistryObject<RuleTestType<PositionRuleTest>> POS_RULE_TEST = RULE_TESTS.register("pos_rule_test", () -> () -> PositionRuleTest.CODEC);
    public static final RegistryObject<RuleTestType<AndRuleTest>> AND_RULE_TEST = RULE_TESTS.register("and_rule_test", () -> () -> AndRuleTest.CODEC);
    public static final RegistryObject<RuleTestType<OrRuleTest>> OR_RULE_TEST = RULE_TESTS.register("or_rule_test", () -> () -> OrRuleTest.CODEC);

}
