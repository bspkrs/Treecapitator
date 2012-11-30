package bspkrs.treecapitator.fml.asm;

/*
 * Invaluable help from AtomicStryker's MultiMine coremod code <3
 */

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;

import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.relauncher.IClassTransformer;

public class ItemInWorldManagerTransformer implements IClassTransformer
{
    /* Obfuscated Names for ItemInWorldManager Transformation */
    
    /* removeBlock */
    private final String  targetMethodDesc = "(III)Z";
    
    private final HashMap obfStrings;
    private final HashMap mcpStrings;
    
    public ItemInWorldManagerTransformer()
    {
        /*
         * create a HashMap to store the obfuscated names of classes, methods, and fields used in the transformation
         */
        obfStrings = new HashMap();
        
        /* 1.4.4 / 1.4.5 mappings */
        /* net.minecraft.src.ItemInWorldManager */
        obfStrings.put("className", "ir");
        /* net/minecraft/src/ItemInWorldManager */
        obfStrings.put("javaClassName", "ir");
        /* removeBlock */
        obfStrings.put("targetMethodName", "d");
        /* theWorld */
        obfStrings.put("worldFieldName", "a");
        /* thisPlayerMP */
        obfStrings.put("entityPlayerFieldName", "b");
        /* net/minecraft/src/World */
        obfStrings.put("worldJavaClassName", "xv");
        /* net/minecraft/src/World.getBlockMetadata() */
        obfStrings.put("getBlockMetadataMethodName", "h");
        /* net/minecraft/src/Block */
        obfStrings.put("blockJavaClassName", "amj");
        /* net/minecraft/src/Block.blocksList[] */
        obfStrings.put("blocksListFieldName", "p");
        /* net/minecraft/src/EntityPlayer */
        obfStrings.put("entityPlayerJavaClassName", "qx");
        /* net/minecraft/src/EntityPlayerMP */
        obfStrings.put("entityPlayerMPJavaClassName", "iq");
        
        /* 1.4.2 mappings */
        /* net.minecraft.src.ItemInWorldManager */
        // obfStrings.put("className", "ii");
        // /* net/minecraft/src/ItemInWorldManager */
        // obfStrings.put("javaClassName", "ii");
        // /* removeBlock */
        // obfStrings.put("targetMethodName", "d");
        // /* theWorld */
        // obfStrings.put("worldFieldName", "a");
        // /* thisPlayerMP */
        // obfStrings.put("entityPlayerFieldName", "b");
        // /* net/minecraft/src/World */
        // obfStrings.put("worldJavaClassName", "xe");
        // /* net/minecraft/src/World.getBlockMetadata() */
        // obfStrings.put("getBlockMetadataMethodName", "g");
        // /* net/minecraft/src/Block */
        // obfStrings.put("blockJavaClassName", "alf");
        // /* net/minecraft/src/Block.blocksList[] */
        // obfStrings.put("blocksListFieldName", "p");
        // /* net/minecraft/src/EntityPlayer */
        // obfStrings.put("entityPlayerJavaClassName", "qg");
        // /* net/minecraft/src/EntityPlayerMP */
        // obfStrings.put("entityPlayerMPJavaClassName", "ih");
        
        /* 1.3.2 mappings */
        /* net.minecraft.src.ItemInWorldManager */
        // obfStrings.put("className", "gv");
        // /* net/minecraft/src/ItemInWorldManager */
        // obfStrings.put("javaClassName", "gv");
        // /* removeBlock */
        // obfStrings.put("targetMethodName", "d");
        // /* theWorld */
        // obfStrings.put("worldFieldName", "a");
        // /* thisPlayerMP */
        // obfStrings.put("entityPlayerFieldName", "b");
        // /* net/minecraft/src/World */
        // obfStrings.put("worldJavaClassName", "up");
        // /* net/minecraft/src/World.getBlockMetadata() */
        // obfStrings.put("getBlockMetadataMethodName", "g");
        // /* net/minecraft/src/Block */
        // obfStrings.put("blockJavaClassName", "aig");
        // /* net/minecraft/src/Block.blocksList[] */
        // obfStrings.put("blocksListFieldName", "m");
        // /* net/minecraft/src/EntityPlayer */
        // obfStrings.put("entityPlayerJavaClassName", "og");
        // /* net/minecraft/src/EntityPlayerMP */
        // obfStrings.put("entityPlayerMPJavaClassName", "gu");
        
        /*
         * create a HashMap to store the MCP names of classes, methods, and fields used in the transformation
         */
        mcpStrings = new HashMap();
        mcpStrings.put("className", "net.minecraft.src.ItemInWorldManager");
        mcpStrings.put("javaClassName", "net/minecraft/src/ItemInWorldManager");
        mcpStrings.put("targetMethodName", "removeBlock");
        mcpStrings.put("worldFieldName", "theWorld");
        mcpStrings.put("entityPlayerFieldName", "thisPlayerMP");
        mcpStrings.put("worldJavaClassName", "net/minecraft/src/World");
        mcpStrings.put("getBlockMetadataMethodName", "getBlockMetadata");
        mcpStrings.put("blockJavaClassName", "net/minecraft/src/Block");
        mcpStrings.put("blocksListFieldName", "blocksList");
        mcpStrings.put("entityPlayerJavaClassName", "net/minecraft/src/EntityPlayer");
        mcpStrings.put("entityPlayerMPJavaClassName", "net/minecraft/src/EntityPlayerMP");
    }
    
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        // System.out.println("transforming: "+name);
        if (name.equals(obfStrings.get("className")))
        {
            return transformItemInWorldManager(bytes, obfStrings);
        }
        else if (name.equals(mcpStrings.get("className")))
        {
            return transformItemInWorldManager(bytes, mcpStrings);
        }
        
        return bytes;
    }
    
    private byte[] transformItemInWorldManager(byte[] bytes, HashMap hm)
    {
        System.out.println("TreeCapitator ASM Magic Time!");
        System.out.println("Class Transformation running on " + hm.get("javaClassName") + "...");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(hm.get("targetMethodName")) && m.desc.equals(targetMethodDesc))
            {
                System.out.println("Found target method " + m.name + m.desc + "! Searching for landmarks...");
                int blockIndex = 4;
                int mdIndex = 5;
                
                // find injection point in method (use IFNULL inst)
                for (int index = 0; index < m.instructions.size(); index++)
                {
                    // System.out.println("Processing INSN at " + index +
                    // " of type " + m.instructions.get(index).getType() +
                    // ", OpCode " + m.instructions.get(index).getOpcode());
                    // find local Block object node and from that, local object
                    // index
                    if (m.instructions.get(index).getType() == AbstractInsnNode.FIELD_INSN)
                    {
                        FieldInsnNode blocksListNode = (FieldInsnNode) m.instructions.get(index);
                        if (blocksListNode.owner.equals(hm.get("blockJavaClassName")) && blocksListNode.name.equals(hm.get("blocksListFieldName")))
                        {
                            int offset = 1;
                            while (m.instructions.get(index + offset).getOpcode() != ASTORE)
                                offset++;
                            System.out.println("Found Block object ASTORE Node at " + (index + offset));
                            VarInsnNode blockNode = (VarInsnNode) m.instructions.get(index + offset);
                            blockIndex = blockNode.var;
                            System.out.println("Block object is in local object " + blockIndex);
                        }
                    }
                    
                    // find local metadata variable node and from that, local
                    // variable index
                    if (m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN)
                    {
                        MethodInsnNode mdNode = (MethodInsnNode) m.instructions.get(index);
                        if (mdNode.owner.equals(hm.get("worldJavaClassName")) && mdNode.name.equals(hm.get("getBlockMetadataMethodName")))
                        {
                            int offset = 1;
                            while (m.instructions.get(index + offset).getOpcode() != ISTORE)
                                offset++;
                            System.out.println("Found metadata local variable ISTORE Node at " + (index + offset));
                            VarInsnNode mdFieldNode = (VarInsnNode) m.instructions.get(index + offset);
                            mdIndex = mdFieldNode.var;
                            System.out.println("Metadata is in local variable " + mdIndex);
                        }
                    }
                    
                    if (m.instructions.get(index).getOpcode() == IFNULL)
                    {
                        System.out.println("Found IFNULL Node at " + index);
                        
                        int offset = 1;
                        while (m.instructions.get(index + offset).getOpcode() != ALOAD)
                            offset++;
                        
                        System.out.println("Found ALOAD Node at offset " + offset + " from IFNULL Node");
                        System.out.println("Patching method " + (String) hm.get("javaClassName") + "/" + m.name + m.desc + "...");
                        
                        // make a new label node for the end of our code
                        LabelNode lmm1Node = new LabelNode(new Label());
                        
                        // make new instruction list
                        InsnList toInject = new InsnList();
                        
                        // construct instruction nodes for list
                        toInject.add(new FieldInsnNode(GETSTATIC, "bspkrs/treecapitator/fml/TreeCapitatorMod", "instance", "Lbspkrs/treecapitator/fml/TreeCapitatorMod;"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, (String) hm.get("javaClassName"), (String) hm.get("worldFieldName"), "L" + (String) hm.get("worldJavaClassName") + ";"));
                        toInject.add(new VarInsnNode(ILOAD, 1));
                        toInject.add(new VarInsnNode(ILOAD, 2));
                        toInject.add(new VarInsnNode(ILOAD, 3));
                        toInject.add(new VarInsnNode(ALOAD, blockIndex));
                        toInject.add(new VarInsnNode(ILOAD, mdIndex));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, (String) hm.get("javaClassName"), (String) hm.get("entityPlayerFieldName"), "L" + (String) hm.get("entityPlayerMPJavaClassName") + ";"));
                        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bspkrs/treecapitator/fml/TreeCapitatorMod", "onBlockHarvested",
                                "(L" + (String) hm.get("worldJavaClassName") + ";IIIL" + (String) hm.get("blockJavaClassName") + ";IL"
                                        + (String) hm.get("entityPlayerJavaClassName") + ";)V"));
                        toInject.add(lmm1Node);
                        
                        m.instructions.insertBefore(m.instructions.get(index + offset), toInject);
                        
                        System.out.println("Method " + (String) hm.get("javaClassName") + "/" + m.name + m.desc + " patched at index " + (index + offset - 1));
                        System.out.println("TreeCapitator ASM Patching Complete!");
                        break;
                    }
                }
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
