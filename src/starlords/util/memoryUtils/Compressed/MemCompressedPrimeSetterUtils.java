package starlords.util.memoryUtils.Compressed;

import starlords.util.ScriptedValues.*;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class MemCompressedPrimeSetterUtils extends MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?,?>>{
    //notes: this is only a class to easier set data in relation to MemCompressedOrganizer without having to go through hell every day of my life.
    private MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?,?>> holder;
    public static MemCompressedPrimeSetterUtils getHolder(String id){
        return new MemCompressedPrimeSetterUtils(id);
    }
    public MemCompressedPrimeSetterUtils(String holderID){
        holder = (MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?, ?>>) MemCompressedMasterList.getMemory().get(holderID);
    }
    public void setBoolean(String key, SV_Boolean_Code item){
        MemCompressedOrganizer<?, SV_Boolean_Code> a = (MemCompressedOrganizer<?, SV_Boolean_Code>) holder.getItem(MTYPE_KEY_BOOLEAN);
        a.setItem(key,item);
    }
    public void setDouble_Code(String key, SV_Double_Code item){
        MemCompressedOrganizer<?, SV_Double_Code> a = (MemCompressedOrganizer<?, SV_Double_Code>) holder.getItem(MTYPE_KEY_DOUBLE);
        a.setItem(key,item);
    }
    public void setString(String key, SV_String_Code item){
        MemCompressedOrganizer<?, SV_String_Code> a = (MemCompressedOrganizer<?, SV_String_Code>) holder.getItem(MTYPE_KEY_STRING);
        a.setItem(key,item);
    }
    public void setObject(String key, SV_Object_Code item){
        MemCompressedOrganizer<?, SV_Object_Code> a = (MemCompressedOrganizer<?, SV_Object_Code>) holder.getItem(MTYPE_KEY_NO_CUSTOM);
        a.setItem(key,item);
    }
}
