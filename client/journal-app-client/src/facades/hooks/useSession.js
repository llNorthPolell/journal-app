import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useSelector, useDispatch} from 'react-redux';
import {useAuth} from '../../contexts/authContext';
import {loadJournalList} from '../../redux/journals';
import {loadJournalEntryList} from '../../redux/journal-entries';

import {setUserId, setCurrentJournal, setCurrentJournalEntry, reset} from '../../redux/session';


const useSession = (requiredOps) => {
    const {journalId, entryId} = useParams();
    const dispatch = useDispatch();

    const { auth } = useAuth();
    const journalEntries = useSelector((state)=>state.journalEntries.journalEntries);
    const journalEntryLoadStatus = useSelector((state)=>state.journalEntries.status);
    const journals = useSelector((state)=>state.journals.journals);
    const journalLoadStatus = useSelector((state)=>state.journals.status);

    const {userId, currentJournal, currentJournalEntry} = useSelector((state)=> state.session);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            if (authUser)
                dispatch(setUserId(authUser.uid));  
        });
        return unsubscribe;
    },[]);


    useEffect(()=>{
        if (journalId && journalLoadStatus==="loaded")
            dispatch(setCurrentJournal(journals.find(journal=> journal.key===journalId)));
        else if (journalId && userId && (journalLoadStatus ==="empty" || journalLoadStatus ==="failed"))
            dispatch(loadJournalList(userId)); 
    }, [journals]);

    useEffect(()=>{
        if (entryId && journalEntryLoadStatus==="loaded")
            dispatch(setCurrentJournalEntry(journalEntries.find(journalEntry=> journalEntry.key===entryId)));
        else if (entryId && journalId && (journalEntryLoadStatus ==="empty" || journalEntryLoadStatus ==="failed"))
            dispatch(loadJournalEntryList(journalId)); 
    }, [journalEntries]);

    let output = [];
    requiredOps.forEach(requiredOp=>{
        switch(requiredOp){
            case "user":
                output = [...output, userId];
                break;
            case "currentJournal":
                output = [...output, currentJournal];
                break;
            case "currentJournalEntry":
                output = [...output, currentJournalEntry];
                break;
            case "reset":
                output = [...output, reset];
                break;
            default:
                break;
        }
    });
    return output;



}
export default useSession;