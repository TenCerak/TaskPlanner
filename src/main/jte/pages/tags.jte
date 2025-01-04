@param java.util.List<com.example.taskplanner.model.Tag> tags
@template.layout(title="Task Planner - Manage Tags", content=@`

    <h1 class="text-center my-4">Manage Tags</h1>

    <form action="/tags/add" method="post" class="mb-4">
        <div class="input-group">
            <input type="text" name="name" class="form-control" placeholder="Tag name" required>
            <button type="submit" class="btn btn-primary">Add Tag</button>
        </div>
    </form>

    <div class="row">
        @for(com.example.taskplanner.model.Tag tag : tags)
            <div class="col-md-4 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${tag.getName()}</h5>
                        <form action="/tags/delete/${tag.getId()}" method="post" style="display:inline;">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        @endfor
    </div>

`)