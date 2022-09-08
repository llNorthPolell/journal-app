import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useSelector, useDispatch} from 'react-redux';
import {loadDashboardConfigList, insertDashboardConfig, setCurrentJournal, reset} from '../../redux/dashboard-configs';

const useDashboardConfigList = (requiredOps) => {
    const {journalId} = useParams();
    const dispatch = useDispatch();

    const {currentJournal,dashboardConfigs,status} = useSelector((state)=>state.dashboardConfigs);

    useEffect(()=>{
        if (status ==="loading" || (currentJournal && journalId===currentJournal.key)) return;
        dispatch(loadDashboardConfigList(journalId));
        dispatch(setCurrentJournal(journalId));
    },[]);

    const createDashboardConfig = async (config)=>{
        const returnDashboardConfig = await dispatch(insertDashboardConfig(config)).unwrap();
        return await returnDashboardConfig;
    }

    let output = [];
    requiredOps.forEach(requiredOp=>{
        switch (requiredOp) {
            case "getAll":
                output = [...output, dashboardConfigs];
                break;
            case "getById":
                break;
            case "loadStatus":
                output = [...output, status];
                break;
            case "insert":
                output = [...output, createDashboardConfig];
                break;
            case "filter":
                break;
            case "update":
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
export default useDashboardConfigList;