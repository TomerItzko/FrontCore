/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.gg;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class GunGameItemStages {
    @NotNull
    public static final List<List<Supplier<? extends GunItem>>> ENTRIES = new ObjectArrayList();

    @NotNull
    public static List<Supplier<? extends GunItem>> getRandomList() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (List<Supplier<? extends GunItem>> list : ENTRIES) {
            objectArrayList.add(list.get((int)(Math.random() * (double)list.size())));
        }
        return objectArrayList;
    }

    static {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(BFItems.GUN_COLT);
        objectArrayList.add(BFItems.GUN_WALTHER_P38);
        objectArrayList.add(BFItems.GUN_MAUSER_M712);
        objectArrayList.add(BFItems.GUN_BERETTA_M1934);
        objectArrayList.add(BFItems.GUN_FN_MODEL_1910);
        objectArrayList.add(BFItems.GUN_LUGER);
        objectArrayList.add(BFItems.GUN_MAUSER_C96);
        objectArrayList.add(BFItems.GUN_TOKAREV_TT33);
        objectArrayList.add(BFItems.GUN_FB_VIS);
        objectArrayList.add(BFItems.GUN_TYPE14);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList);
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        objectArrayList2.add(BFItems.GUN_M1_CARBINE);
        objectArrayList2.add(BFItems.GUN_GEWEHR_43);
        objectArrayList2.add(BFItems.GUN_TOKAREV_SVT40);
        objectArrayList2.add(BFItems.GUN_M1_GARAND);
        objectArrayList2.add(BFItems.GUN_CARCANO_M91TS_CARBINE);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList2);
        ObjectArrayList objectArrayList3 = new ObjectArrayList();
        objectArrayList3.add(BFItems.GUN_KAR98K);
        objectArrayList3.add(BFItems.GUN_MOSIN_NAGANT);
        objectArrayList3.add(BFItems.GUN_LEE_ENFIELD_MK1);
        objectArrayList3.add(BFItems.GUN_SPRINGFIELD);
        objectArrayList3.add(BFItems.GUN_CARCANO_M38);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList3);
        ObjectArrayList objectArrayList4 = new ObjectArrayList();
        objectArrayList4.add(BFItems.GUN_M1A1_THOMPSON);
        objectArrayList4.add(BFItems.GUN_MP40);
        objectArrayList4.add(BFItems.GUN_STEN_MK2);
        objectArrayList4.add(BFItems.GUN_STG44);
        objectArrayList4.add(BFItems.GUN_PPSH);
        objectArrayList4.add(BFItems.GUN_MP41);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList4);
        ObjectArrayList objectArrayList5 = new ObjectArrayList();
        objectArrayList5.add(BFItems.GUN_BAR);
        objectArrayList5.add(BFItems.GUN_BREN_MK2);
        objectArrayList5.add(BFItems.GUN_DP28);
        objectArrayList5.add(BFItems.GUN_MG34);
        objectArrayList5.add(BFItems.GUN_ZB26);
        objectArrayList5.add(BFItems.GUN_TYPE11);
        objectArrayList5.add(BFItems.GUN_MODEL_1930);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList5);
        ObjectArrayList objectArrayList6 = new ObjectArrayList();
        objectArrayList6.add(BFItems.GUN_BROWNING30);
        objectArrayList6.add(BFItems.GUN_MG42);
        objectArrayList6.add(BFItems.GUN_TYPE92);
        objectArrayList6.add(BFItems.GUN_VICKERS_K);
        objectArrayList6.add(BFItems.GUN_FIAT_REVELLI);
        objectArrayList6.add(BFItems.GUN_MAC_MLE_1931);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList6);
        ObjectArrayList objectArrayList7 = new ObjectArrayList();
        objectArrayList7.add(BFItems.GUN_TRENCHGUN);
        objectArrayList7.add(BFItems.GUN_M30);
        objectArrayList7.add(BFItems.GUN_BECKER);
        objectArrayList7.add(BFItems.GUN_TYPE18_SHOTGUN);
        objectArrayList7.add(BFItems.GUN_BROWNING_A5);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList7);
        ObjectArrayList objectArrayList8 = new ObjectArrayList();
        objectArrayList8.add(BFItems.GUN_BAZOOKA);
        objectArrayList8.add(BFItems.GUN_PANZERSCHRECK);
        objectArrayList8.add(BFItems.GUN_PANZERFAUST);
        objectArrayList8.add(BFItems.GUN_PIAT);
        objectArrayList8.add(BFItems.GUN_TYPE4_70MM);
        ENTRIES.add((List<Supplier<? extends GunItem>>)objectArrayList8);
    }
}

