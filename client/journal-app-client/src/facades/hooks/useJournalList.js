import {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {loadJournalList, insertJournal, updateJournal, setUserId, reset} from '../../redux/journals';
import {useAuth} from '../../contexts/authContext';

const useJournalList = (requiredOps) => {
    const { auth } = useAuth();

    const dispatch = useDispatch();

    const {userId,journals,status} = useSelector((state)=>state.journals);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            if (authUser){
                const authUid = authUser.uid;
                if (userId!==authUid)
                    dispatch(setUserId(authUid));
                if (status ==="empty" || status ==="failed")
                    dispatch(loadJournalList(authUid));
            }
        });
        return unsubscribe;
    },[]);

    const createJournal = async (journal)=>{
        const returnJournal = await dispatch(insertJournal({...journal,author:userId})).unwrap();
        return returnJournal;
    }

    const getJournalDoc = (journalId) =>{
        console.log("Finding " + journalId + " in " + JSON.stringify(journals));
        return journals.find(journal=>{return journal.key===journalId})
    }

    const editJournal = async(journalId, payload) => {
        const returnJournal = await dispatch(updateJournal(journalId,payload)).unwrap();
        return returnJournal;
    }

    let output = [];
    requiredOps.forEach(requiredOp => {
        switch (requiredOp) {
            case "getAll":
                output = [...output, journals];
                break;
            case "getById":
                output = [...output, getJournalDoc];
                break;
            case "loadStatus":
                output = [...output, status];
                break;
            case "insert":
                output = [...output, createJournal];
                break;
            case "filter":
                break;
            case "update":
                output = [...output, editJournal];
                break;
            case "delete":
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
export default useJournalList;