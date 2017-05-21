package com.jayavery.jjmod.tileentities;

import com.jayavery.jjmod.init.ModItems;
import com.jayavery.jjmod.init.ModPackets;
import com.jayavery.jjmod.items.ItemEdible;
import com.jayavery.jjmod.packets.CompostPacketClient;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

/** TileEntity for compost heap. */
public class TECompost extends TEContainerAbstract implements ITickable {

    /** Total number of ticks to produce each output. */
    public static final int COMPOST_EACH = 6000;
    /** Maximum absolute value of ingredient balance. */
    public static final int MAX_BALANCE = 72;
    /** Maximum input fullness. */
    public static final int MAX_INPUT = 12;
    /** Balance threshold for quality 5. */
    private static final int BALANCE_5 = 2;
    /** Balance threshold for quality 4. */
    private static final int BALANCE_4 = 12;
    /** Balance threshold for quality 3. */
    private static final int BALANCE_3 = 27;
    /** Balance threshold for quality 2. */
    private static final int BALANCE_2 = 47;
    
    /** Ticks spend composting next output. */
    private int compostSpent = 0;
    /** Balance of nitrogen (negative) to carbon (positive). */
    private int balance = 0;
    /** This compost heap's input fullness. */
    private int input = 0;
    /** This compost heap's output. */
    public NonNullList<ItemStack> outputs = NonNullList
            .withSize(1, ItemStack.EMPTY);

    /** @return Whether this stack is a nitrogen input. */
    public static boolean isCarbon(ItemStack stack) {
        
        Item item = stack.getItem();
        return item instanceof ItemEdible || item == ModItems.wool ||
                item == Items.BONE;
    }
    
    /** @return Whether this stack is a carbon input. */
    public static boolean isNitrogen(ItemStack stack) {
        
        Item item = stack.getItem();
        return item == ModItems.leaves || item == Items.STICK ||
                item == ModItems.log || item == ModItems.pole ||
                item == ModItems.thicklog || item == Items.REEDS;
    }
    
    @Override
    public void dropItems() {

        this.dropInventory(this.outputs);
    }
    
    /** @return The ticks spent processing the current piece. */
    public int getCompostSpent() {
        
        return this.compostSpent;
    }
    
    /** @return The current output stack. */
    public ItemStack getOutput() {
        
        return this.outputs.get(0);
    }
    
    /** @return The current input fullness. */
    public int getInput() {
        
        return this.input;
    }
    
    /** @return The current ingredient balance. */
    public int getBalance() {
        
        return this.balance;
    }
    
    /** Adds this stack to affect the compost balance and input. */
    public void addInput(ItemStack stack) {
        
        int count = stack.getCount();
        this.input += count;
        this.input = Math.min(this.input, MAX_INPUT);
        
        if (isNitrogen(stack)) {
            
            this.balance -= count;
            
        } else if (isCarbon(stack)) {
            
            this.balance += count;
        }
        
        this.balance = MathHelper.clamp(this.balance,
                -MAX_BALANCE, MAX_BALANCE);
        
        int bal = Math.abs(this.balance);
        int grade = bal <= BALANCE_5 ? 5 : bal <= BALANCE_4 ?
                4 : bal <= BALANCE_3 ? 3 : bal <= BALANCE_2 ? 2 : 1;
        this.outputs.set(0, new ItemStack(ModItems.compost,
                this.outputs.get(0).getCount(), grade));
    }
    
    @Override
    public void update() {

        if (this.world.isRemote) {
            
            return;
        }
                
        if (this.outputs.get(0).getCount() < 12 && this.input > 0) {
            
            this.compostSpent++;
            
            if (this.compostSpent >= COMPOST_EACH) {
                
                this.compostSpent = 0;
                this.input--;
                this.markDirty();
                this.outputs.get(0).grow(1);
            }
            
        } else if (this.compostSpent > 0) {
            
            this.compostSpent = 0;
        }
        
        this.sendProgressPacket();
    }
    
    /** Sets the given values, used for packets. */
    public void setValues(int input, int compostSpent,
            int balance, ItemStack output) {
        
        this.input = input;
        this.compostSpent = compostSpent;
        this.balance = balance;
        this.outputs.set(0, output);
    }
    
    /** Sends a packet to update progress values on the client. */
    private void sendProgressPacket() {
        
        if (!this.world.isRemote) {
        
            ModPackets.NETWORK.sendToAll(new CompostPacketClient(this.input,
                    this.compostSpent, this.balance,
                    this.outputs.get(0), this.pos));
        }
    }
    
    /** Required to update GUI on the client. */
    @Override
    public NBTTagCompound getUpdateTag() {

        return this.writeToNBT(new NBTTagCompound());
    }

    /** Required to update GUI on the client. */
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        return new SPacketUpdateTileEntity(this.getPos(), 0,
                this.writeToNBT(new NBTTagCompound()));
    }

    /** Required to update GUI on the client. */
    @Override
    public void onDataPacket(NetworkManager net,
            SPacketUpdateTileEntity packet) {

        this.readFromNBT(packet.getNbtCompound());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        
        super.readFromNBT(compound);
        
        this.balance = compound.getInteger("balance");
        this.input = compound.getInteger("input");
        this.outputs.set(0, new ItemStack(compound.getCompoundTag("output")));
        this.compostSpent = compound.getInteger("compostSpent");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        
        super.writeToNBT(compound);
        
        compound.setInteger("balance", this.balance);
        compound.setInteger("input", this.input);
        compound.setTag("output", this.outputs.get(0)
                .writeToNBT(new NBTTagCompound()));
        compound.setInteger("compostSpent", this.compostSpent);
        
        return compound;
    }
}