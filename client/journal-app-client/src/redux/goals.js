import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { createGoalDoc, getGoalDocs, getGoalDoc, updateGoalDoc } from '../facades/data';


export const loadGoalList = createAsyncThunk('goals/loadGoals', async (journalId)=> {
    console.log("in loadGoalList...");
    let resultGoalDocs = await getGoalDocs(journalId);
    return resultGoalDocs.map(goal=> ({...goal,journal:journalId}));
})

export const insertGoal = createAsyncThunk('goals/insertGoal', async (newGoal)=> {
    console.log("in insertGoal...");
    const {journal,...returnGoal} = await createGoalDoc(newGoal);
    return returnGoal;
})

export const updateGoal = createAsyncThunk('goals/updateGoal', async ({goalId,payload})=> {
    console.log("in updateGoal...");
    const {journal,...returnGoal} = await updateGoalDoc(goalId, payload);
    return returnGoal;
})

const initialState = {
    goals:[],
    status: "empty",
    error: "",
    currentJournal: null
}

export const goalsSlice = createSlice({
  name: 'goalList',
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
    builder.addCase(loadGoalList.pending, (state)=>{
        console.log("in loadGoalList.pending...");
        state.status = "loading";
    }) 
    builder.addCase(loadGoalList.fulfilled, (state, action)=>{
        console.log("in loadGoalList.fulfilled...");
        state.goals = action.payload;
        state.status = "loaded";
        state.error = "";
    })
    builder.addCase(loadGoalList.rejected,(state,action)=>{
        console.log("in loadGoalList.rejected...");
        state.status = "failed";
        state.error=action.error.message
        console.error("ERROR: "+action.error.message);
    })
    builder.addCase(insertGoal.pending,(state)=>{
        console.log("in insertGoal.pending...");
        state.status="updating";
    })
    builder.addCase(insertGoal.fulfilled, (state, action)=> {
        console.log("in insertGoal.fulfilled...");
        state.goals.push(action.payload);
        state.error = "";
        state.status="loaded";
    })
    builder.addCase(updateGoal.pending,(state)=>{
        console.log("in updateGoal.pending...");
        state.status="updating";
    })
    builder.addCase(updateGoal.fulfilled,(state,action)=>{
        console.log("in updateGoal.fulfilled...");
        state.goals = state.goals.map(goal=> goal.key===action.payload.key? action.payload:goal);
        state.error = "";
        state.status="loaded";
    })
  }
})

export const {setCurrentJournal,reset} = goalsSlice.actions;

export default goalsSlice.reducer