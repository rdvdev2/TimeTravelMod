package tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.village;

import com.google.common.collect.Lists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StructureVillageOldWestPieces {
    public static void registerVillageOldWestPieces() {
        // Register the village pieces here
    }

    public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size)
    {
        List<StructureVillagePieces.PieceWeight> list = Lists.<StructureVillagePieces.PieceWeight>newArrayList();
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getInt(random, 0 + size, 1 + size)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.getInt(random, 2 + size, 5 + size * 3)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.getInt(random, 1 + size, 4 + size)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.getInt(random, 0, 1 + size)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.getInt(random, 0 + size, 3 + size * 2)));
        Iterator<StructureVillagePieces.PieceWeight> iterator = list.iterator();

        while (iterator.hasNext())
        {
            if ((iterator.next()).villagePiecesLimit == 0)
            {
                iterator.remove();
            }
        }

        return list;
    }
}
