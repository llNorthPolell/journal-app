import { configureStore } from '@reduxjs/toolkit'
/*import journalsReducer from './journals'
import journalEntriesReducer from './journal-entries';
import dashboardConfigsReducer from './dashboard-configs';
import goalsReducer from './goals';*/

export const store = configureStore({
  reducer: {
/*    journals: journalsReducer,
    journalEntries: journalEntriesReducer,
    dashboardConfigs: dashboardConfigsReducer,
    goals: goalsReducer*/
  },
})
