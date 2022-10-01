import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createJournalDoc, getJournalDocs, getJournalDoc, updateJournalDoc } from '../facades/data';

export const loadJournalList = createAsyncThunk('journals/loadJournals', async (userId)=> {
    console.log("in loadJournalList...");
    return await getJournalDocs(userId);
})

export const insertJournal = createAsyncThunk('journals/insertJournal', async (newJournal)=> {
    console.log("in insertJournal...");
    return await createJournalDoc(newJournal);
})

export const updateJournal = createAsyncThunk('journals/updateJournal', async ({journalId,payload})=> {
    console.log("in updateJournal...")
    return await updateJournalDoc(journalId, payload);
})

const initialState = {
    journals:[],
    status: "empty",
    error: "",
    userId: ""
}

export const journalsSlice = createSlice({
  name: "journals",
  initialState,
  reducers: {
    setUserId: (state,action) => { 
        state.userId= action.payload
    },
    reset: (state)=>{
        state = initialState
    }
  },
  extraReducers: (builder)=> {
    builder.addCase(loadJournalList.pending, (state)=>{
        console.log("in loadJournalList.pending...");
        state.status = "loading";
    }) 
    builder.addCase(loadJournalList.fulfilled, (state, action)=>{
        console.log("in loadJournalList.fulfilled...");
        state.journals = action.payload;
        state.status = "loaded";
        state.error = "";
        console.log("journalList state: "+JSON.stringify(state));
    })
    builder.addCase(loadJournalList.rejected,(state,action)=>{
        console.log("in loadJournalList.rejected...");
        state.status = "failed";
        state.error=action.error.message
        console.error("ERROR: "+action.error.message);
    })
    builder.addCase(insertJournal.pending,(state)=>{
        console.log("in insertJournal.pending...");
        state.status="updating";
    })
    builder.addCase(insertJournal.fulfilled, (state, action)=> {
        console.log("in insertJournal.fulfilled...");
        state.journals.push(action.payload);
        state.error = "";
        state.status="loaded";
    })
    builder.addCase(updateJournal.pending,(state)=>{
        console.log("in updateJournal.pending...");
        state.status="updating";
    })
    builder.addCase(updateJournal.fulfilled,(state,action)=>{
        console.log("in updateJournal.fulfilled...");
        state.journals = state.journals.map(journal=> journal.key===action.payload.key? action.payload:journal);
        state.error = "";
        state.status="loaded";
    })
  }
})

export const {setUserId,reset} = journalsSlice.actions;

export default journalsSlice.reducer