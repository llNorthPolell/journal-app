import { createSlice } from '@reduxjs/toolkit';


const initialState = {
    userId:null,
    currentJournal:null,
    currentJournalEntry: null
}

export const sessionSlice = createSlice({
  name: "session",
  initialState,
  reducers: {
    setUserId: (state,action) => { 
        console.log("in session.setUserId...");
        state.userId= action.payload
        console.log("Set Session User: " + action.payload);
    },
    setCurrentJournal: (state,action) => { 
        console.log("in session.setCurrentJournal...");
        state.currentJournal= action.payload
        console.log("Set Session Journal: " + JSON.stringify(action.payload));
    },    
    setCurrentJournalEntry: (state,action) => { 
        console.log("in session.setCurrentJournalEntry...");
        state.currentJournalEntry= action.payload
        console.log("Set Session Journal Entry: " + JSON.stringify(action.payload));
    },  
    reset: (state)=>{
        state = initialState
    }
  }
})

export const {setUserId,setCurrentJournal,setCurrentJournalEntry,reset} = sessionSlice.actions;

export default sessionSlice.reducer