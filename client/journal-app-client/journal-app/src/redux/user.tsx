import { createSlice} from '@reduxjs/toolkit';
import {UserState} from '../models/user';

const initialState : UserState = {
    user:null,
    loggedIn:false
}

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state,action) => { 
        console.log("in user.setUser...");
        state.user= {
          uid: action.payload.uid,
          accessToken:action.payload.accessToken
        };
        if (state.user)
          state.loggedIn=true;
        console.log("Set User: " + action.payload.uid);
    },
    reset: (state)=>{
        state = initialState
    }
  }
})

export const {setUser,reset} = userSlice.actions;

export default userSlice.reducer;