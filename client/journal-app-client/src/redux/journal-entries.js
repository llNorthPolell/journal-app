import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createJournalEntryDoc, getJournalEntryDocs, getJournalEntryDoc, updateJournalEntryDoc } from '../facades/data';


export const loadJournalEntryList = createAsyncThunk('journalEntries/loadJournalEntries', async (journalId)=> {
    console.log("in loadJournalEntryList...");
    let resultJournalEntryDocs = await getJournalEntryDocs(journalId);
    return resultJournalEntryDocs.map(journalEntry=> ({...journalEntry,journal:journalId}));
})

export const insertJournalEntry = createAsyncThunk('journalEntries/insertJournalEntry', async (newJournalEntry)=> {
    console.log("in insertJournalEntry...");
    const {journal,...returnJournalEntry} = await createJournalEntryDoc(newJournalEntry);
    return returnJournalEntry;
})

export const updateJournalEntry = createAsyncThunk('journalEntries/updateJournalEntry', async (journalEntryId,payload)=> {
    console.log("in updateJournalEntry...");
    await updateJournalEntryDoc(journalEntryId, payload);
    return await getJournalEntryDoc(journalEntryId);
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
    builder.addCase(insertJournalEntry.fulfilled, (state, action)=> {
        console.log("in insertJournalEntry.fulfilled...");
        state.journalEntries.push(action.payload);
        state.error = "";
    })
    builder.addCase(updateJournalEntry.fulfilled,(state,action)=>{
        console.log("in updateJournalEntry.fulfilled...");
        state.journalEntries = state.journalEntries.map(journalEntry=> journalEntry.key===action.payload.key? action.payload:journalEntry);
        state.error = "";
    })
  }
})

export const {setCurrentJournal,reset} = journalEntriesSlice.actions;

export default journalEntriesSlice.reducer