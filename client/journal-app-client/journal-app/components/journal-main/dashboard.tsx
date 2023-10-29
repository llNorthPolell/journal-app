'use client'
import { useState } from "react";
import { Journal } from "../../models/journal"
import { PuzzleGridContainer } from "../../../../../../../PuzzleGrid/puzzle-grid/src/puzzle-grid-container/puzzle-grid-container";
import { PuzzleBlock } from "../../../../../../../PuzzleGrid/puzzle-grid/src/puzzle-grid-container/puzzle-block/puzzle-block";

interface DashboardProps {
    journal: Journal
}

const testWidgets = [
    {
        id: "0a5fbc04-3aae-4f4e-b681-02a71e8e3984",
        width:1,
        height:1,
        text: "Widget 1 by 1",
        index: 0
    },
    {
        id: "0eb7a4e3-081e-4843-9862-914e438e5284",
        width:1,
        height:1,
        text: "Widget 1 by 1",
        index: 1
    },
    {
        id: "fb83383e-2336-43a3-9ebc-0a811f6498b0",
        width:1,
        height:2,
        text: "Widget 1 by 2",
        index: 2
    },
    {
        id: "e2e66070-84b1-4430-8994-58213a77c35e",
        width:2,
        height:1,
        text: "Widget 2 by 1",
        index: 3
    },
    {
        id: "907bace9-0135-42c0-80bb-a273612bd3d6",
        width:2,
        height:2,
        text: "Widget 2 by 2",
        index: 4
    }
]


export default function Dashboard(props: DashboardProps) {
    const [widgets, setWidgets] = useState(testWidgets);

    const handleDragDrop = (results: any) => {
        const { source, destination, type } = results;

        if (!destination) return;

        if (source.droppableId === destination.droppableId
            && source.index === destination.index) return;

        if (type === "group") {
            const newWidgetOrder = [...widgets];
            const [removedWidget] = newWidgetOrder.splice(source.index, 1);
            newWidgetOrder.splice(destination.index, 0, removedWidget);
            setWidgets(newWidgetOrder);
            console.log("Drop " + source.index + " to " + destination.index);
        }


    }


    return (
        <div className="dashboard">
            <div className="dashboard__controls">
                <p>Dashboard Controls</p>
            </div>
            {
                /*
            <PuzzleGridContainer>
            {
                    widgets.map(widget => (

                        <PuzzleBlock
                            key={widget.id}
                            id={widget.id}
                            index={widget.index}
                            draggable={true}
                            widthSpan={widget.width}
                            heightSpan={widget.height}>
                            {widget.text}
                        </PuzzleBlock>


                    ))
            }
            </PuzzleGridContainer>
            */
        }
        </div>
    )
}