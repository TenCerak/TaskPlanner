@param java.util.List<com.example.taskplanner.model.Category> categories
@template.layout(title="Task Planner - Manage Categories", content=@`

    <h1 class="text-center my-4">Manage Categories</h1>

    <form action="/categories/add" method="post" class="mb-4">
        <div class="input-group">
            <input type="text" name="name" class="form-control" placeholder="Category name" required>
            <button type="submit" class="btn btn-primary">Add Category</button>
        </div>
    </form>

    <div class="row">
        @for(com.example.taskplanner.model.Category category : categories)
            <div class="col-md-4 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${category.getName()}</h5>
                        <form action="/categories/delete/${category.getId()}" method="post" style="display:inline;">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        @endfor
    </div>

`)