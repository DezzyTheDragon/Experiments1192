package io.github.dezzythedragon.experiments1192.screen;

import io.github.dezzythedragon.experiments1192.Experiments1192;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Experiments1192.MODID);

    public static final RegistryObject<MenuType<ElectrumRefineryMenu>> ELECTRUM_REFINERY = MENUS.register("electrum_refinery_menu",
            () -> IForgeMenuType.create(ElectrumRefineryMenu::new));

    public static final RegistryObject<MenuType<ComplexAssemblerMenu>> COMPLEX_ASSEMBLER = MENUS.register("complex_assembler_menu",
            () -> IForgeMenuType.create(ComplexAssemblerMenu::new));

    /*
    public static final RegistryObject<MenuType<StoryTabletMenu>> STORY_TABLET = MENUS.register("story_tablet_menu",
            () -> IForgeMenuType.create(StoryTabletMenu::new));
     */

    public static void RegisterMenus(){
        MenuScreens.register(MenuRegistry.ELECTRUM_REFINERY.get(), ElectrumRefineryScreen::new);
        MenuScreens.register(MenuRegistry.COMPLEX_ASSEMBLER.get(), ComplexAssemblerScreen::new);
        //MenuScreens.register(MenuRegistry.STORY_TABLET.get(), StoryTabletScreen::new);
    }
}
