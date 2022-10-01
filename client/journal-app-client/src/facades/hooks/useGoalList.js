import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useSelector, useDispatch} from 'react-redux';
import {loadGoalList, insertGoal, updateGoal, setCurrentJournal, reset} from '../../redux/goals';

const useGoalList = (requiredOps) => {
    const {journalId} = useParams();
    const dispatch = useDispatch();

    const {currentJournal,goals,status} = useSelector((state)=>state.goals);

    useEffect(()=>{
        if (status ==="loading" || (currentJournal && journalId===currentJournal.key)) return;
        dispatch(setCurrentJournal(journalId));
        dispatch(loadGoalList(journalId)); 
    },[])

    const createGoal = async (newGoal)=>{
        const returnGoal = await dispatch(insertGoal(newGoal)).unwrap();
        return returnGoal;
    }

    const getGoal = (key)=>{
        return goals.find(goal=> goal.key===key);
    }


    const filterGoals = (searchInput) => {
        let searchResults = [];

        return searchResults;
    }



    const editGoal = async(goalId, payload) => {
        const returnGoal = await dispatch(updateGoal({goalId: goalId,payload:payload})).unwrap();
        return returnGoal;
    }

    let output = [];
    requiredOps.forEach(requiredOp=>{
        switch(requiredOp){
            case "getAll":
                output = [...output, goals];
                break;
            case "getById":
                output = [...output, getGoal];
                break;
            case "loadStatus":
                output = [...output, status];
                break;
            case "insert":
                output = [...output, createGoal];
                break;
            case "filter":
                output = [...output, filterGoals];
                break;
            case "update":
                output = [...output, editGoal];
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
export default useGoalList;