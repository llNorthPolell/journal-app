import React, {useContext, useState, useEffect} from 'react';
import {useAuth} from '../contexts/authContext';
import {journalRef,journalEntriesRef, getList, query, where, createDoc} from '../firebase'
import listUtil from '../util/functions/list-util';

const DataContext = React.createContext();

export function useData() {
    return useContext(DataContext);
}

export function DataProvider ({children}){
    const { user } = useAuth();
    const [journalList, setJournalList] = useState([]);
    const [userId, setUserId] = useState();

    useEffect(() => {
        if (user != null) {
            setUserId(user.uid);
        } else {
            setUserId();
        }
    }, [user]);

    useEffect(()=>{
        if (userId!=null){
            const createdJournalsQuery = query(journalRef, where("author", "==", userId));
            getList(createdJournalsQuery).then((journalDocs)=>{
                listUtil(journalList, setJournalList, { type: "SET", payload:journalDocs});
            });      
        } 
    },[userId])



    function createJournal(journal){
        createDoc(journalRef,journal).then((docRef)=>{
            let returnId = docRef.id;
            console.log("New Journal ID: " +returnId);
            listUtil(journalList, setJournalList, { type: "INSERT", payload:{...journal,key: returnId}});
        }).catch((err)=>{
            console.log(err.message);
        });
    }

    function createJournalEntry(journalEntry){
        createDoc(journalEntriesRef,journalEntry).then((docRef)=>{
            let returnId = docRef.id;
            console.log("New Journal Entry ID: " +returnId);
        }).catch((err)=>{
            console.log(err.message);
        });
    }


    const values = {
        journalList, createJournal, userId
    }

    
    return (
        <DataContext.Provider value={values}>
            {children}
        </DataContext.Provider>
    )

}

