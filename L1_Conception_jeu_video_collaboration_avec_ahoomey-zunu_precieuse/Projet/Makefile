CFLAGS = -O3 `sdl2-config --cflags`
LDFLAGS = `sdl2-config --libs` -lSDL2_ttf -lSDL2_mixer -lm

INC = sdl2-light.h constantes.h world.h graphiques.h sdl2-ttf-light.h
SRC = main.c sdl2-light.c world.c graphiques.c sdl2-ttf-light.c
OBJ = $(SRC:%.c=%.o)

PROG = spacecorridor

%.o: %.c $(INC)
	gcc $(CFLAGS) -c $<

spacecorridor: $(OBJ)
	gcc $(CFLAGS) $(OBJ) $(LDFLAGS) -o $@

doc: $(PROG)
	doxygen ./$(PROG)
	make -C latex

clean:
	rm -f *~ *.o $(PROG)
	rm -rf latex html

