import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createJournalEntryDoc, getJournalEntryDocs, getJournalEntryDoc, updateJournalEntryDoc } from '../facades/data';


export const loadJournalEntryList = createAsyncThunk('journalEntries/loadJournalEntries', async (journalId)=> {
    console.log("in loadJournalEntryList...");
    let resultJournalEntryDocs = await getJournalEntryDocs(journalId);
    resultJournalEntryDocs.sort((a,b)=>{return a.dateOfEntry<b.dateOfEntry});
    return resultJournalEntryDocs.map(journalEntry=> ({...journalEntry,journal:journalId}));
})

export const insertJournalEntry = createAsyncThunk('journalEntries/insertJournalEntry', async (newJournalEntry)=> {
    console.log("in insertJournalEntry...");
    const {journal,...returnJournalEntry} = await createJournalEntryDoc(newJournalEntry);
    return returnJournalEntry;
})

export const updateJournalEntry = createAsyncThunk('journalEntries/updateJournalEntry', async ({journalEntryId,payload})=> {
    console.log("in updateJournalEntry...");
    const {journal,...returnJournalEntry} = await updateJournalEntryDoc(journalEntryId, payload);
    return returnJournalEntry;
})

const initialState = {
    journalEntries:[],
    status: "empty",
    error: "",
    currentJournal: null
}

export const journalEntriesSlice = createSlice({
  name: 'journalEntryList',
  initialState,
  reducers: {
    setCurrentJournal: (state,action)=> {
        state.currentJournal=action.payload
    },
    reset: (state)=>{
        state = initialState
    }
  },
  extraReducers: (builder)=> {
    builder.addCase(loadJournalEntryList.pending, (state)=>{
        console.log("in loadJournalEntryList.pending...");
        state.status = "loading";
    }) 
    builder.addCase(loadJournalEntryList.fulfilled, (state, action)=>{
        console.log("in loadJournalEntryList.fulfilled...");
        state.journalEntries = action.payload;
        state.status = "loaded";
        state.error = "";
    })
    builder.addCase(loadJournalEntryList.rejected,(state,action)=>{
        console.log("in loadJournalEntryList.rejected...");
        state.status = "failed";
        state.error=action.error.message
        console.error("ERROR: "+action.error.message);
    })
    builder.addCase(insertJournalEntry.pending,(state)=>{
        console.log("in insertJournalEntry.pending...");
        state.status="updating";
    })
    builder.addCase(insertJournalEntry.fulfilled, (state, action)=> {
        console.log("in insertJournalEntry.fulfilled...");
        state.journalEntries.push(action.payload);
        state.error = "";
        state.status="loaded";
    })
    builder.addCase(updateJournalEntry.fulfilled,(state,action)=>{
        console.log("in updateJournalEntry.fulfilled...");
        state.journalEntries = state.journalEntries.map(journalEntry=> journalEntry.key===action.payload.key? action.payload:journalEntry);
        state.error = "";
        state.status="loaded";
    })
    builder.addCase(updateJournalEntry.pending,(state)=>{
        console.log("in updateJournalEntry.pending...");
        state.status="updating";
    })
  }
})

export const {setCurrentJournal,reset} = journalEntriesSlice.actions;

export default journalEntriesSlice.reducer