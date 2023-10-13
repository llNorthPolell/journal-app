import { Journal } from "../../models/journal"

interface DashboardProps{
    journal : Journal
}

export default function Dashboard (props:DashboardProps){

    return (
        <h1>Dashboard for {props.journal.name} </h1>
    )
}