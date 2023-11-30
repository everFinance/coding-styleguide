package demo

type Demo struct {
}

func New(arNode, dsn string) *Demo {
	return &Demo{}
}

func (d *Demo) Run() {}

func (d *Demo) Close() {}
